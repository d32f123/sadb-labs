package com.itmo.db.generator.generator.entity;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.model.entity.NumericallyIdentifiableEntity;
import com.itmo.db.generator.pool.EntityPool;
import com.itmo.db.generator.pool.EntityPoolInstance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// WARN: Generator MUST NOT modify dependency instances
@Slf4j
public abstract class AbstractEntityGenerator<T extends AbstractEntity<TId>, TId> implements EntityGenerator {

    private static int currentId;

    protected final Generator generator;
    protected EntityPool<T, TId> pool;
    private final EntityDefinition<T, TId> entity;
    protected final Map<Class<? extends AbstractEntity<?>>, DependencyWithMeta<?, ?>> dependenciesMetaMap;
    private int instancesCreated;
    private final Object instanceCreationMonitor;
    private final Object runMonitor;
    private boolean shouldContinue;
    private boolean noDependantsLeft;

    @Data
    @AllArgsConstructor
    protected static class DependencyWithMeta<T extends AbstractEntity<TId>, TId> implements Serializable {
        private Class<T> entityClass;
        private List<T> entityValues;
        private EntityPoolInstance<T, TId> poolInstance;
        private int amount;
    }

    public AbstractEntityGenerator(EntityDefinition<T, TId> entity,
                                   Generator generator) {
        log.debug("Creating AbstractEntityGenerator for '{}' with deps: '{}'", entity.getEntityClass(), entity.getDependencies());
        this.generator = generator;
        this.entity = entity;
        this.dependenciesMetaMap = new HashMap<>(entity.getDependencies().size());
        this.instanceCreationMonitor = new Object();
        this.runMonitor = new Object();
        this.pool = this.generator.getEntityPool(entity.getEntityClass());
    }

    @Override
    public void run() {
        log.debug("Running AbstractEntityGenerator for '{}' with deps: '{}'", entity.getEntityClass(), entity.getDependencies());
        entity.getDependencies().forEach(dep ->
                this.dependenciesMetaMap.put(
                        dep.getDependencyClass(),
                        new DependencyWithMeta(
                                dep.getDependencyClass(),
                                null,
                                this.generator.getEntityPool(dep.getDependencyClass()).getInstance(entity.getEntityClass()),
                                dep.getAmountPerInstance()
                        )
                )
        );

        while (entity.getAmount() != null && entity.getAmount() < pool.getAvailableAmount() && !this.noDependantsLeft) {
            log.trace("'{}' loop: '{}' entities so far", entity.getEntityClass(), pool.getAvailableAmount());
            if (this.dependenciesMetaMap.isEmpty()) {
                if (this.pool.getAvailableAmount() >= entity.getAmount()) {
                    log.info("All entities generated for '{}'. Total: '{}'", entity.getEntityClass(), pool.getAvailableAmount());
                    this.pool.freeze();
                    return;
                }
                this.shouldContinue = true;
                log.trace("No deps for '{}'", entity.getEntityClass());
                if (entity.getAmount() == null) {
                    log.error("No deps for '{}' but no amount either!", entity.getEntityClass());
                    throw new IllegalArgumentException("No dependencies but no amount specified either");
                }
                log.trace("'{}': Passing control to implementation", entity.getEntityClass());
                this.doGenerate();
                continue;
            }

            this.shouldContinue = false;
            log.trace("Deps found for '{}'", entity.getEntityClass());
            this.instancesCreated = 0;
            this.dependenciesMetaMap.values().forEach(depWithMeta -> {
                log.debug("AbstractEntityGenerator for '{}': Requesting amount '{}' of '{}'",
                        entity.getEntityClass(), depWithMeta.amount, depWithMeta.entityClass);
                depWithMeta.getPoolInstance().request(depWithMeta.getAmount(), (entities) -> {
                    log.trace("Got entities for '{}'", depWithMeta.entityClass);
                    synchronized (this.runMonitor) {
                        if (this.noDependantsLeft) {
                            return;
                        }
                        if (entities == null || entities.size() != depWithMeta.getAmount()) {
                            log.info("No dependants left for '{}'. Total generated: '{}'. Exiting now",
                                    entity.getEntityClass(), pool.getAvailableAmount());
                            this.noDependantsLeft = true;
                            this.shouldContinue = true;
                            this.pool.freeze();
                            this.runMonitor.notify();
                            return;
                        }
                            }
                            synchronized (this.instanceCreationMonitor) {
                                depWithMeta.setEntityValues((List) entities);
                                this.instancesCreated += 1;
                                if (!this.checkIfAllDependenciesInstantiated()) {
                                    log.trace("'{}': Still waiting for some entities...", entity.getEntityClass());
                                    return;
                                }
                            }
                    log.debug("'{}': Got all entities, passing control to implementation", entity.getEntityClass());
                    this.doGenerate();
                            synchronized (this.runMonitor) {
                                log.trace("'{}': Acquired lock, notifying main runner", entity.getEntityClass());
                                this.shouldContinue = true;
                                this.runMonitor.notify();
                                log.trace("'{}': Notified main thread", entity.getEntityClass());
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
        return this.instancesCreated == this.entity.getDependencies().size();
    }

    protected <TDep extends AbstractEntity<TDepId>, TDepId> List<TDep> getDependencyInstances(Class<TDep> dependencyClass) {
        return (List<TDep>) this.dependenciesMetaMap.get(dependencyClass).getEntityValues();
    }

    private void doGenerate() {
        List<T> entities = this.getEntities();
        if (entities == null || entities.isEmpty()) {
            return;
        }

        entities.forEach(entity -> {
            if (entity instanceof NumericallyIdentifiableEntity) {
                NumericallyIdentifiableEntity numericallyIdentifiableEntity = (NumericallyIdentifiableEntity) entity;
                if (numericallyIdentifiableEntity.getId() == null) {
                    int id = getEntityId();
                    log.debug("'{}': Generated id for numerically identifiable entity", id);
                    numericallyIdentifiableEntity.setId(id);
                }
            }
            pool.add(entity);
        });
    }

    protected abstract List<T> getEntities();

    private synchronized static int getEntityId() {
        int returnValue = currentId;
        currentId += 1;
        return returnValue;
    }
}
