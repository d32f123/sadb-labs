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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class EntityPoolImpl<T extends AbstractEntity<TId>, TId> implements EntityPool<T, TId> {
    private int availableAmount;
    private boolean frozen;

    private final EventBus eventBus;
    private final List<T> entities;
    private final ReadWriteLock entitiesLock;

    private final Map<Class<?>, EntityPoolInstanceImpl<T, TId>> poolInstances;

    private final Class<T> entityClass;

    public EntityPoolImpl(Class<T> entityClass) {
        this.eventBus = EventBus.getInstance();
        this.availableAmount = 0;
        this.entities = new ArrayList<>(1024);
        this.entitiesLock = new ReentrantReadWriteLock();
        this.poolInstances = new HashMap<>();
        this.entityClass = entityClass;
        this.frozen = false;
    }


    @Override
    public int getAvailableAmount() {
        this.entitiesLock.readLock().lock();
        int amount = this.availableAmount;
        this.entitiesLock.readLock().unlock();
        return amount;
    }

    @Override
    public boolean isFrozen() {
        this.entitiesLock.readLock().lock();
        boolean frozen = this.frozen;
        this.entitiesLock.readLock().unlock();
        return frozen;
    }

    @Override
    public void add(T entity) {
        if (log.isDebugEnabled())
            log.debug("Adding entityClass '{}'. {} entities so far", entity, availableAmount);
        if (this.frozen) {
            log.warn("Trying to add to a frozen pool: '{}'", entityClass);
            return;
        }

        this.availableAmount += 1;

        this.entitiesLock.writeLock().lock();
        this.entities.add(entity);
        this.entitiesLock.writeLock().unlock();
        this.eventBus.notify(GeneratorEvent.ENTITY_GENERATED, new GeneratorEventMessage<>(entityClass, entity));
    }

    @Override
    public List<T> getPool() {
        return this.entities;
    }

    @Override
    public Lock getReadLock() {
        return this.entitiesLock.readLock();
    }

    @Override
    public EntityPoolInstance<T, TId> getInstance(Class<?> requester) {
        if (this.poolInstances.containsKey(requester)) {
            if (log.isInfoEnabled())
                log.info("Got entity pool instance for '{}:{}' from cache", entityClass, requester);
            return this.poolInstances.get(requester);
        }
        if (log.isInfoEnabled())
            log.info("Got new entity pool instance for '{}:{}'", entityClass, requester);
        this.poolInstances.put(requester, new EntityPoolInstanceImpl<>(this, this.entityClass, requester));
        return this.poolInstances.get(requester);
    }

    @Override
    public synchronized void freeze() {
        if (this.frozen) {
            if (log.isInfoEnabled())
                log.info("Pool '{}' is already frozen, skipping", entityClass);
            return;
        }
        if (log.isInfoEnabled())
            log.info("Freezing '{}' at '{}' entities", entityClass, availableAmount);
        this.frozen = true;
        this.eventBus.notify(GeneratorEvent.ENTITY_GENERATION_FINISHED, new GeneratorEventMessage<>(entityClass, this.getPool()));
    }
}
