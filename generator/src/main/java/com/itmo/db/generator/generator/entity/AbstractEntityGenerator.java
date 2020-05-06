package com.itmo.db.generator.generator.entity;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.pool.EntityPool;

public abstract class AbstractEntityGenerator<T extends AbstractEntity> implements EntityGenerator {

    protected final Generator generator;
    protected EntityPool<T> pool;
    private final Class<T> entityClass;

    public AbstractEntityGenerator(Class<T> entityClass, Generator generator) {
        this.generator = generator;
        this.entityClass = entityClass;
    }

    @Override
    public void run() {
        this.pool = this.generator.getEntityPool(entityClass);

        while (pool.getActualAmount() < pool.getRequestedAmount()) {
            T entity = getEntity();
            pool.add(entity);
        }
    }

    protected abstract T getEntity();
}
