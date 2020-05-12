package com.itmo.db.generator.generator.model;

import com.itmo.db.generator.generator.entity.EntityGenerator;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.persistence.PersistenceWorker;
import com.itmo.db.generator.pool.EntityPool;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GeneratableEntity<T extends AbstractEntity> {
    @NonNull
    private Class<? extends AbstractEntity> entityClass;
    @NonNull
    private EntityPool<T> pool;
    private EntityGenerator generator;
    private Thread generatorThread;
    private PersistenceWorker persistenceWorker;
    private Thread persistenceWorkerThread;
    private boolean generated = false;
    private boolean persisted = false;
}
