package com.itmo.db.generator.pool;

import com.itmo.db.generator.model.entity.AbstractEntity;

import java.util.List;

public interface EntityPool<T extends AbstractEntity<TId>, TId> {
    int getRequestedAmount();
    void setRequestedAmount(int amount);

    int getActualAmount();

    boolean isFrozen();

    void add(T entity);
    List<T> getPool();
    EntityPoolInstance<T, TId> getInstance(Class<?> requester);
}
