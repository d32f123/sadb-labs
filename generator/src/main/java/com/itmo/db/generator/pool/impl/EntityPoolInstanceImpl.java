package com.itmo.db.generator.pool.impl;

import com.itmo.db.generator.generator.event.GenerationEvent;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.pool.EntityPool;
import com.itmo.db.generator.pool.EntityPoolInstance;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

// TODO: Rewrite via Thread scheduling every N seconds
public class EntityPoolInstanceImpl<T extends AbstractEntity> implements EntityPoolInstance<T> {

    private final EntityPool<T> pool;
    private int pointer;
    private final Queue<ConsumerWithMeta> consumerQueue;

    @Data
    @AllArgsConstructor
    private class ConsumerWithMeta {
        public Consumer<List<T>> consumer;
        int entitiesCount;
    }

    public EntityPoolInstanceImpl(EntityPool<T> pool) {
        this.pool = pool;
        this.pointer = 0;
        this.consumerQueue = new ConcurrentLinkedQueue<>();
        this.pool.subscribe(GenerationEvent.ENTITY_GENERATED, this::poolUpdatedCallback);
        this.pool.subscribe(GenerationEvent.ENTITY_GENERATION_FINISHED, this::poolGenerationCompleteCallback);
    }

    @Override
    public void request(int entitiesCount, Consumer<List<T>> callback) {
        this.consumerQueue.add(new ConsumerWithMeta(callback, entitiesCount));
    }

    private int getNumberOfAvailableEntities() {
        return this.pool.getActualAmount() - this.pointer;
    }

    private void poolUpdatedCallback(GenerationEvent event, T entity) {
        while (true) {
            ConsumerWithMeta consumer = consumerQueue.peek();

            if (consumer == null) {
                return;
            }

            if (this.pool.isFrozen() && this.getNumberOfAvailableEntities() < consumer.entitiesCount) {
                consumerQueue.poll();
                continue;
            }

            if (this.getNumberOfAvailableEntities() < consumer.entitiesCount) {
                return;
            }

            consumerQueue.poll();

            int entitiesRetrievedCount = consumer.entitiesCount;
            List<T> entitiesRetrieved = this.pool.getPool().subList(pointer, pointer + entitiesRetrievedCount);
            pointer += entitiesRetrievedCount;
            consumer.consumer.accept(entitiesRetrieved);
        }
    }

    private void poolGenerationCompleteCallback(GenerationEvent event, T entity) {

    }

}
