package com.itmo.db.generator.generator.model;

import com.itmo.db.generator.generator.entity.EntityGenerator;
import com.itmo.db.generator.mapper.EntityToDAOMapper;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.persistence.PersistenceWorker;
import com.itmo.db.generator.pool.EntityPool;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Future;

@Data
@RequiredArgsConstructor
public class GeneratableEntity<T extends AbstractEntity<TId>, TId> {
    @NonNull
    private Class<T> entityClass;
    @NonNull
    private EntityPool<T, TId> pool;
    @NonNull
    private EntityToDAOMapper<T, TId> mapper;
    private EntityGenerator generator;
    private Future<?> generatorThread;
    private PersistenceWorker persistenceWorker;
    private Future<?> persistenceWorkerThread;
    private boolean generated = false;
    private boolean persisted = false;
}
