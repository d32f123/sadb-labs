package com.itmo.db.generator.pool.impl;

import com.itmo.db.generator.utils.eventbus.EventBus;
import com.itmo.db.generator.utils.eventbus.GeneratorEvent;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.pool.EntityPool;
import com.itmo.db.generator.pool.EntityPoolInstance;
import com.itmo.db.generator.utils.eventbus.GeneratorEventMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class EntityPoolImpl<T extends AbstractEntity> implements EntityPool<T> {

    private int requestedAmount;
    private int actualAmount;
    private boolean frozen;

    private final EventBus eventBus;
    private final List<T> entities;

    private final Map<Class<?>, EntityPoolInstanceImpl<T>> poolInstances;

    private final Class<T> entityClass;

    public EntityPoolImpl(Class<T> entityClass) {
        this(entityClass, 1000);
    }

    public EntityPoolImpl(Class<T> entityClass, int requestedAmount) {
        this.eventBus = EventBus.getInstance();
        this.requestedAmount = requestedAmount;
        this.actualAmount = 0;
        this.entities = new ArrayList<>(requestedAmount / 4);
        this.poolInstances = new HashMap<>();
        this.entityClass = entityClass;
        this.frozen = false;
    }

    @Override
    public synchronized int getRequestedAmount() {
        return this.requestedAmount;
    }

    @Override
    public synchronized void setRequestedAmount(int amount) {
        this.requestedAmount = amount;
    }

    @Override
    public synchronized int getActualAmount() {
        return this.actualAmount;
    }

    @Override
    public synchronized boolean isFrozen() {
        return this.frozen;
    }

    @Override
    public synchronized void add(T entity) {
        if (this.actualAmount >= this.requestedAmount) {
            log.error("Adding entityClass will lead to overflow");
            if (!this.frozen) {
                this.frozen = true;
                this.eventBus.notify(GeneratorEvent.ENTITY_GENERATION_FINISHED, this.getPool());
            }
            return;
        }
        log.debug("Adding entityClass '{}'. {} / {}", entity, actualAmount, requestedAmount);
        this.actualAmount += 1;

        this.entities.add(entity);
        this.eventBus.notify(GeneratorEvent.ENTITY_GENERATED, new GeneratorEventMessage<>(entityClass, entity));

        if (this.actualAmount >= this.requestedAmount) {
            log.info("Total amount of entities reached '{}'", entityClass);
            this.frozen = true;
            this.eventBus.notify(GeneratorEvent.ENTITY_GENERATION_FINISHED, new GeneratorEventMessage<>(entityClass, this.getPool()));
        }
    }

    @Override
    public List<T> getPool() {
        return this.entities;
    }

    @Override
    public EntityPoolInstance<T> getInstance(Class<?> requester) {
        if (this.poolInstances.containsKey(requester)) {
            return this.poolInstances.get(requester);
        }
        this.poolInstances.put(requester, new EntityPoolInstanceImpl<>(this, this.entityClass));
        return this.poolInstances.get(requester);
    }
}
