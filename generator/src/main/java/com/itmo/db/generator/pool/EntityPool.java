package com.itmo.db.generator.pool;

import com.itmo.db.generator.model.entity.AbstractEntity;

import java.util.List;
import java.util.concurrent.locks.Lock;

public interface EntityPool<T extends AbstractEntity<TId>, TId> {
    int getAvailableAmount();

    boolean isFrozen();

    void add(T entity);

    List<T> getPool();

    Lock getReadLock();

    EntityPoolInstance<T, TId> getInstance(Class<?> requester);

    void freeze();
}
