package com.itmo.db.generator.pool.impl;

import com.itmo.db.generator.eventmanager.EventManager;
import com.itmo.db.generator.generator.event.GenerationEvent;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.pool.EntityPool;
import com.itmo.db.generator.pool.EntityPoolInstance;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EntityPoolImpl<T extends AbstractEntity> extends EventManager<GenerationEvent, T> implements EntityPool<T> {

    private int requestedAmount;
    private int actualAmount;
    private boolean frozen;

    private final List<T> entities;

    public EntityPoolImpl() {
        this(1000);
    }

    public EntityPoolImpl(int requestedAmount) {
        this.requestedAmount = requestedAmount;
        this.actualAmount = 0;
        this.entities = new ArrayList<>(requestedAmount / 4);
        this.frozen = false;
    }

    @Override
    public int getRequestedAmount() {
        return this.requestedAmount;
    }

    @Override
    public void setRequestedAmount(int amount) {
        this.requestedAmount = amount;
    }

    @Override
    public int getActualAmount() {
        return this.actualAmount;
    }

    @Override
    public boolean isFrozen() {
        return this.frozen;
    }

    @Override
    public void add(T entity) {
        if (this.actualAmount >= this.requestedAmount) {
            log.warn("Adding entityClass will lead to overflow");
            this.frozen = true;
            return;
        }
        log.info("Adding entityClass '{}'. {} / {}", entity, actualAmount, requestedAmount);
        this.actualAmount += 1;

        this.entities.add(entity);
        this.notify(GenerationEvent.ENTITY_GENERATED, entity);

        if (this.actualAmount >= this.requestedAmount) {
            log.info("Total amount of entities reached");
            this.frozen = true;
            this.notify(GenerationEvent.ENTITY_GENERATION_FINISHED, null);
        }
    }

    @Override
    public EntityPoolInstance<T> getInstance(Class<?> requester) {
        return null;
    }
}
