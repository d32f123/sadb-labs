package com.itmo.db.generator.pool.impl;

import com.itmo.db.generator.utils.eventbus.EventBus;
import com.itmo.db.generator.utils.eventbus.GeneratorEvent;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.pool.EntityPool;
import com.itmo.db.generator.pool.EntityPoolInstance;
import com.itmo.db.generator.utils.eventbus.GeneratorEventMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
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
        this.eventBus.subscribe(GeneratorEvent.ENTITY_GENERATED, entityClass, this::poolUpdatedCallback);
        this.eventBus.subscribe(GeneratorEvent.ENTITY_GENERATION_FINISHED, entityClass, this::poolGenerationCompleteCallback);
    }

    @Override
    public void request(int entitiesCount, Consumer<List<T>> callback) {
        log.debug("'{}': Got request for {} entities", entityClass, entitiesCount);
        this.consumerQueue.add(new ConsumerWithMeta(callback, entitiesCount));
        new Thread(this::handleQueue).start();
    }

    private synchronized int getNumberOfAvailableEntities() {
        return this.pool.getActualAmount() - this.pointer;
    }

    private synchronized void handleQueue() {
        log.debug("'{}': HandleQueue running", entityClass);
        while (true) {
            ConsumerWithMeta consumer = consumerQueue.peek();

            if (consumer == null) {
                log.warn("'{}': Peeked at queue but no consumer found, whoops?", entityClass);
                return;
            }

            if (this.pool.isFrozen() && this.getNumberOfAvailableEntities() < consumer.entitiesCount) {
                log.debug("'{}': Pool is already frozen. Sub requests {} but available {}. Sending null",
                        entityClass, consumer.entitiesCount, this.getNumberOfAvailableEntities());
                this.sendToConsumer(consumerQueue.poll(), null);
                continue;
            }

            if (this.getNumberOfAvailableEntities() < consumer.entitiesCount) {
                log.debug("'{}': Pool is not frozen and not enough entities as of yet ({} / {})",
                        entityClass, this.getNumberOfAvailableEntities(), consumer.entitiesCount);
                return;
            }

            log.debug("'{}': Enough entities available", entityClass);
            consumerQueue.poll();

            int entitiesRetrievedCount = consumer.entitiesCount;
            List<T> entitiesRetrieved = List.copyOf(this.pool.getPool().subList(pointer, pointer + entitiesRetrievedCount));
            pointer += entitiesRetrievedCount;
            log.debug("'{}': Acquired {} entities", entityClass, consumer.entitiesCount);
            this.sendToConsumer(consumer, entitiesRetrieved);
        }
    }

    private synchronized void sendToConsumer(ConsumerWithMeta consumer, List<T> entities) {
        new Thread(() -> consumer.getConsumer().accept(entities)).start();
    }

    private synchronized void poolUpdatedCallback(GeneratorEventMessage<T, TId, T> message) {
        this.handleQueue();
    }

    // TODO: Unsub here from everything
    private void poolGenerationCompleteCallback(GeneratorEventMessage<T, TId, ?> message) {
        this.handleQueue();
    }

}