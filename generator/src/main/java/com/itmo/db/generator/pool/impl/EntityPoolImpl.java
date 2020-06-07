package com.itmo.db.generator.pool.impl;

import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.pool.EntityPool;
import com.itmo.db.generator.pool.EntityPoolInstance;
import com.itmo.db.generator.utils.eventbus.EventBus;
import com.itmo.db.generator.utils.eventbus.GeneratorEvent;
import com.itmo.db.generator.utils.eventbus.GeneratorEventMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class EntityPoolImpl<T extends AbstractEntity<TId>, TId> implements EntityPool<T, TId> {
    private int availableAmount;
    private boolean frozen;

    private final EventBus eventBus;
    private final List<T> entities;

    private final Map<Class<?>, EntityPoolInstanceImpl<T, TId>> poolInstances;

    private final Class<T> entityClass;

    public EntityPoolImpl(Class<T> entityClass) {
        this.eventBus = EventBus.getInstance();
        this.availableAmount = 0;
        this.entities = new ArrayList<>(1024);
        this.poolInstances = new HashMap<>();
        this.entityClass = entityClass;
        this.frozen = false;
    }

    @Override
    public synchronized int getAvailableAmount() {
        return this.availableAmount;
    }

    @Override
    public synchronized boolean isFrozen() {
        return this.frozen;
    }

    @Override
    public synchronized void add(T entity) {
        log.debug("Adding entityClass '{}'. {} entities so far", entity, availableAmount);
        if (this.frozen) {
            log.warn("Trying to add to a frozen pool: '{}'", entityClass);
            return;
        }

        this.availableAmount += 1;

        this.entities.add(entity);
        this.eventBus.notify(GeneratorEvent.ENTITY_GENERATED, new GeneratorEventMessage<>(entityClass, entity));
    }

    @Override
    public List<T> getPool() {
        return this.entities;
    }

    @Override
    public EntityPoolInstance<T, TId> getInstance(Class<?> requester) {
        if (this.poolInstances.containsKey(requester)) {
            return this.poolInstances.get(requester);
        }
        this.poolInstances.put(requester, new EntityPoolInstanceImpl<>(this, this.entityClass));
        return this.poolInstances.get(requester);
    }

    @Override
    public void freeze() {
        log.info("Freezing '{}' at '{}' entities", entityClass, availableAmount);
        this.frozen = true;
        this.eventBus.notify(GeneratorEvent.ENTITY_GENERATION_FINISHED, new GeneratorEventMessage<>(entityClass, this.getPool()));
    }
}
