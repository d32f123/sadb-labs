package com.itmo.db.generator.pool;

import com.itmo.db.generator.eventmanager.IEventManager;
import com.itmo.db.generator.generator.event.GenerationEvent;
import com.itmo.db.generator.model.entity.AbstractEntity;

import java.util.List;

public interface EntityPool<T extends AbstractEntity> extends IEventManager<GenerationEvent, T> {
    int getRequestedAmount();
    void setRequestedAmount(int amount);

    int getActualAmount();

    boolean isFrozen();

    void add(T entity);
    List<T> getPool();
    EntityPoolInstance<T> getInstance(Class<?> requester);
}
