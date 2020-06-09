package com.itmo.db.generator.pool.impl;

import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.pool.EntityPool;
import com.itmo.db.generator.pool.EntityPoolInstance;
import com.itmo.db.generator.pool.ThreadPoolFactory;
import com.itmo.db.generator.utils.eventbus.EventBus;
import com.itmo.db.generator.utils.eventbus.GeneratorEvent;
import com.itmo.db.generator.utils.eventbus.GeneratorEventMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

@Slf4j
public class EntityPoolInstanceImpl<T extends AbstractEntity<TId>, TId> implements EntityPoolInstance<T, TId> {

    private final EntityPool<T, TId> pool;
    private final EventBus eventBus;
    private int pointer;
    private final Queue<ConsumerWithMeta> consumerQueue;
    private final Class<T> entityClass;
    private final Object handlerMonitor;
    private boolean isHandlerStarted = false;
    private boolean shouldExit = false;

    @Data
    @AllArgsConstructor
    private class ConsumerWithMeta {
        public Consumer<List<T>> consumer;
        int entitiesCount;
    }

    public EntityPoolInstanceImpl(EntityPool<T, TId> pool, Class<T> entityClass) {
        this.pool = pool;
        this.pointer = 0;
        this.consumerQueue = new ConcurrentLinkedQueue<>();
        this.entityClass = entityClass;
        this.eventBus = EventBus.getInstance();
        this.handlerMonitor = new Object();
        ThreadPoolFactory.getPool(ThreadPoolFactory.ThreadPoolType.POOL_INSTANCE).submit(this::handleQueue);
        this.eventBus.subscribe(GeneratorEvent.ENTITY_GENERATED, entityClass, this::poolUpdatedCallback);
        this.eventBus.subscribe(GeneratorEvent.ENTITY_GENERATION_FINISHED, entityClass, this::poolGenerationCompleteCallback);
    }

    @Override
    public void request(int entitiesCount, Consumer<List<T>> callback) {
        if (log.isDebugEnabled())
            log.debug("'{}': Got request for {} entities", entityClass, entitiesCount);
        var consumer = new ConsumerWithMeta(callback, entitiesCount);
        synchronized (this.handlerMonitor) {
            if (this.pool.isFrozen() && this.getNumberOfAvailableEntities() < entitiesCount) {
                if (log.isInfoEnabled())
                    log.info("'{}': Pool is already frozen and not enough entities available. Sending null", entityClass);
                this.sendToConsumer(consumer, null);
                return;
            }
            if (!this.isHandlerStarted) {
                log.warn("'{}': Handler is not started yet. Waiting", entityClass);
                this.waitForMonitor();
            }
            if (log.isDebugEnabled())
                log.debug("'{}': Notifying main thread main thread", entityClass);
            this.consumerQueue.offer(consumer);
            this.handlerMonitor.notifyAll();
        }
    }

    private int getNumberOfAvailableEntities() {
        return this.pool.getAvailableAmount() - this.pointer;
    }

    private void handleQueue() {
        synchronized (handlerMonitor) {
            this.isHandlerStarted = true;
            this.handlerMonitor.notifyAll();
        }
        while (!shouldExit) {
            if (log.isDebugEnabled())
                log.debug("'{}': HandleQueue iteration", entityClass);
            ConsumerWithMeta consumer;
            synchronized (handlerMonitor) {
                consumer = consumerQueue.peek();

                if (consumer == null) {
                    if (log.isTraceEnabled()) {
                        log.trace("'{}': Peeked at queue but no consumer found", entityClass);
                    }
                    if (this.pool.isFrozen() && this.getNumberOfAvailableEntities() == 0) {
                        if (log.isInfoEnabled())
                            log.info("'{}': Stopping as no consumers and already frozen", entityClass);
                        return;
                    }
                    this.waitForMonitor();
                    continue;
                }

                if (this.pool.isFrozen() && this.getNumberOfAvailableEntities() == 0) {
                    if (log.isDebugEnabled())
                        log.debug("'{}': Pool is already frozen. Sub requests {} but none available. Sending null",
                                entityClass, consumer.entitiesCount);
                    this.sendToConsumer(consumerQueue.poll(), null);
                    continue;
                }

                if (!this.pool.isFrozen() && this.getNumberOfAvailableEntities() < consumer.entitiesCount) {
                    if (log.isDebugEnabled())
                        log.debug("'{}': Pool is not frozen and not enough entities as of yet ({} / {})",
                                entityClass, this.getNumberOfAvailableEntities(), consumer.entitiesCount);
                    this.waitForMonitor();
                    continue;
                }
            }

            int entitiesRetrievedCount = Math.min(consumer.entitiesCount, this.getNumberOfAvailableEntities());
            if (log.isDebugEnabled())
                log.debug("'{}': Enough entities available ({} / {})", entityClass, entitiesRetrievedCount, consumer.entitiesCount);
            synchronized (this.handlerMonitor) {
                consumerQueue.poll();
            }

            List<T> entitiesRetrieved = List.copyOf(this.pool.getPool().subList(pointer, pointer + entitiesRetrievedCount));
            pointer += entitiesRetrievedCount;
            if (log.isDebugEnabled())
                log.debug("'{}': Acquired {} entities", entityClass, consumer.entitiesCount);
            this.sendToConsumer(consumer, entitiesRetrieved);
        }
    }

    private void waitForMonitor() {
        try {
            this.handlerMonitor.wait();
        } catch (InterruptedException ignored) {
            this.shouldExit = true;
        }
    }

    private void sendToConsumer(ConsumerWithMeta consumer, List<T> entities) {
        ThreadPoolFactory.getPool(ThreadPoolFactory.ThreadPoolType.MISC).submit(
                () -> consumer.getConsumer().accept(entities)
        );
    }

    private void poolUpdatedCallback(GeneratorEventMessage<T, TId, T> message) {
        synchronized (this.handlerMonitor) {
            if (this.isHandlerStarted) {
                this.handlerMonitor.notifyAll();
            }
        }
    }

    // TODO: Unsub here from everything
    private void poolGenerationCompleteCallback(GeneratorEventMessage<T, TId, ?> message) {
        synchronized (this.handlerMonitor) {
            if (this.isHandlerStarted) {
                this.handlerMonitor.notifyAll();
            }
        }
    }

}
