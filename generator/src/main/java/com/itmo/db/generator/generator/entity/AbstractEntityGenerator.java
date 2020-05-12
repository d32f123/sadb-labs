package com.itmo.db.generator.generator.entity;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.pool.EntityPool;
import com.itmo.db.generator.pool.EntityPoolInstance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;

// WARN: Generator MUST NOT modify dependency instances
@Slf4j
public abstract class AbstractEntityGenerator<T extends AbstractEntity> implements EntityGenerator {

    protected final Generator generator;
    protected EntityPool<T> pool;
    private final Class<T> entityClass;
    private final Set<DependencyDefinition> dependencies;
    protected final Map<Class<? extends AbstractEntity>, DependencyWithMeta> dependenciesMetaMap;
    private int instancesCreated;
    private final Object instanceCreationMonitor;
    private final Object runMonitor;
    private boolean shouldContinue;

    @Data
    @AllArgsConstructor
    protected static class DependencyWithMeta implements Serializable {
        private Class<? extends AbstractEntity> entityClass;
        private List<? extends AbstractEntity> entityValues;
        private EntityPoolInstance<? extends AbstractEntity> poolInstance;
        private int amount;
    }


    public AbstractEntityGenerator(Class<T> entityClass,
                                   Set<DependencyDefinition> dependencies,
                                   Generator generator) {
        log.debug("Creating AbstractEntityGenerator for '{}' with deps: '{}'", entityClass, dependencies);
        this.generator = generator;
        this.entityClass = entityClass;
        this.dependencies = new HashSet<>(dependencies);
        this.dependenciesMetaMap = new HashMap<>(dependencies.size());
        this.instanceCreationMonitor = new Object();
        this.runMonitor = new Object();
        this.pool = this.generator.getEntityPool(entityClass);
    }

    @Override
    public void run() {
        log.debug("Running AbstractEntityGenerator for '{}' with deps: '{}'", entityClass, dependencies);
        this.dependencies.forEach(dep ->
                this.dependenciesMetaMap.put(
                        dep.getDependencyClass(),
                        new DependencyWithMeta(
                                dep.getDependencyClass(),
                                null,
                                this.generator.getEntityPool(dep.getDependencyClass()).getInstance(this.entityClass),
                                dep.getAmountPerInstance()
                        )
                )
        );

        while (pool.getActualAmount() < pool.getRequestedAmount()) {
            log.trace("'{}' loop: '{}' / '{}'", entityClass, pool.getActualAmount(), pool.getRequestedAmount());
            if (this.dependenciesMetaMap.isEmpty()) {
                this.shouldContinue = true;
                log.trace("No deps for '{}'", entityClass);
                log.trace("'{}': Passing control to implementation", entityClass);
                T entity = this.getEntity();
                pool.add(entity);
                continue;
            }

            this.shouldContinue = false;
            log.trace("Deps found for '{}'", entityClass);
            this.instancesCreated = 0;
            this.dependenciesMetaMap.values().forEach(depWithMeta -> {
                        log.debug("AbstractEntityGenerator for '{}': Requesting amount '{}' of '{}'",
                                entityClass, depWithMeta.amount, depWithMeta.entityClass);
                        depWithMeta.getPoolInstance().request(depWithMeta.getAmount(), (entities) -> {
                            log.trace("Got entities for '{}'", depWithMeta.entityClass);
                            synchronized (this.instanceCreationMonitor) {
                                depWithMeta.setEntityValues(entities);
                                this.instancesCreated += 1;
                                if (!this.checkIfAllDependenciesInstantiated()) {
                                    log.trace("'{}': Still waiting for some entities...", entityClass);
                                    return;
                                }
                            }
                            log.debug("'{}': Got all entities, passing control to implementation", entityClass);
                            T entity = this.getEntity();
                            pool.add(entity);
                            synchronized (this.runMonitor) {
                                log.trace("'{}': Acquired lock, notifying main runner", entityClass);
                                this.shouldContinue = true;
                                this.runMonitor.notify();
                                log.trace("'{}': Notified main thread", entityClass);
                            }
                        });
                    }
            );
            synchronized (this.runMonitor) {
                while (!this.shouldContinue) {
                    try {
                        this.runMonitor.wait();
                    } catch (InterruptedException ignored) {}
                }
            }
        }
    }

    private boolean checkIfAllDependenciesInstantiated() {
        return this.instancesCreated == this.dependencies.size();
    }

    protected <TDep extends AbstractEntity> List<TDep> getDependencyInstances(Class<TDep> dependencyClass) {
        return (List<TDep>) this.dependenciesMetaMap.get(dependencyClass).getEntityValues();
    }

    protected abstract T getEntity();
}
