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
import java.util.Random;

// WARN: Generator MUST NOT modify dependency instances
@Slf4j
public abstract class AbstractEntityGenerator<T extends AbstractEntity<TId>, TId> implements EntityGenerator {

    private static int currentId;

    protected final Generator generator;
    protected EntityPool<T, TId> pool;
    private final EntityDefinition<T, TId> entity;
    protected final Map<Class<? extends AbstractEntity<?>>, DependencyWithMeta<?, ?>> dependenciesMetaMap;
    private int instancesCreated;
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

    protected Random random = new Random();

    public AbstractEntityGenerator(EntityDefinition<T, TId> entity,
                                   Generator generator) {
        if (log.isDebugEnabled())
            log.debug("Creating AbstractEntityGenerator for '{}' with deps: '{}'", entity.getEntityClass(), entity.getDependencies());
        this.generator = generator;
        this.entity = entity;
        this.dependenciesMetaMap = new HashMap<>(entity.getDependencies().size());
        this.runMonitor = new Object();
        this.pool = this.generator.getEntityPool(entity.getEntityClass());
    }

    @Override
    public void run() {
        if (log.isInfoEnabled())
            log.info("Running AbstractEntityGenerator for '{}' with deps: '{}'", entity.getEntityClass(), entity.getDependencies());
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

        while ((entity.getAmount() == null || pool.getAvailableAmount() < entity.getAmount()) && !this.noDependantsLeft) {
            if (log.isTraceEnabled()) {
                log.trace("'{}' loop: '{}' entities so far", entity.getEntityClass(), pool.getAvailableAmount());
            }
            if (pool.getAvailableAmount() % 100 == 0)
                log.info("Processing {} enttities. {} of {} ", entity.getEntityClass().getName(), pool.getAvailableAmount(), entity.getAmount());

            if (this.dependenciesMetaMap.isEmpty()) {
                if (log.isTraceEnabled()) {
                    log.trace("No deps for '{}'", entity.getEntityClass());
                }

                if (entity.getAmount() == null) {
                    log.error("No deps for '{}' but no amount either!", entity.getEntityClass());
                    throw new IllegalArgumentException("No dependencies but no amount specified either");
                }
                if (log.isTraceEnabled()) {
                    log.trace("'{}': Passing control to implementation", entity.getEntityClass());
                }
                this.doGenerate();
                continue;
            }

            this.shouldContinue = false;
            if (log.isTraceEnabled()) {
                log.trace("Deps found for '{}'", entity.getEntityClass());
            }
            this.instancesCreated = 0;
            synchronized (this.runMonitor) {
                this.dependenciesMetaMap.values().forEach(depWithMeta -> {
                            if (log.isDebugEnabled())
                                log.debug("AbstractEntityGenerator for '{}': Requesting amount '{}' of '{}'",
                                        entity.getEntityClass(), depWithMeta.amount, depWithMeta.entityClass);
                            depWithMeta.getPoolInstance().request(depWithMeta.getAmount(), (entities) -> {
                                if (log.isTraceEnabled()) {
                                    log.trace("Got entities for '{}'", depWithMeta.entityClass);
                                }
                                synchronized (this.runMonitor) {
                                    if (this.noDependantsLeft) {
                                        this.shouldContinue = true;
                                        this.runMonitor.notify();
                                        return;
                                    }
                                    if (entities == null || entities.size() != depWithMeta.getAmount()) {
                                        if (log.isInfoEnabled())
                                            log.info("No dependants left for '{}'. Total generated: '{}'. Exiting now",
                                                    entity.getEntityClass(), pool.getAvailableAmount());
                                        this.noDependantsLeft = true;
                                        this.shouldContinue = true;
                                        this.runMonitor.notify();
                                        return;
                                    }
                                    depWithMeta.setEntityValues((List) entities);
                                    this.instancesCreated += 1;
                                    if (!this.checkIfAllDependenciesInstantiated()) {
                                        if (log.isTraceEnabled()) {
                                            log.trace("'{}': Still waiting for some entities...", entity.getEntityClass());
                                        }
                                        return;
                                    }
                                }
                                if (log.isDebugEnabled()) {
                                    log.debug("'{}': Got all entities, passing control to implementation", entity.getEntityClass());
                                }
                                this.doGenerate();
                                synchronized (this.runMonitor) {
                                    if (log.isTraceEnabled()) {
                                        log.trace("'{}': Acquired lock, notifying main runner", entity.getEntityClass());
                                    }
                                    this.shouldContinue = true;
                                    this.runMonitor.notify();
                                    if (log.isTraceEnabled()) {
                                        log.trace("'{}': Notified main thread", entity.getEntityClass());
                                    }
                                }
                            });
                        }
                );

                while (!this.shouldContinue) {
                    try {
                        this.runMonitor.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
        if (log.isInfoEnabled())
            log.info("All entities generated for '{}'. Total: '{}'", entity.getEntityClass(), pool.getAvailableAmount());
        this.pool.freeze();
    }

    private boolean checkIfAllDependenciesInstantiated() {
        return this.instancesCreated == this.entity.getDependencies().size();
    }

    protected <TDep extends AbstractEntity<TDepId>, TDepId> List<TDep> getDependencyInstances(Class<TDep> dependencyClass) {
        return (List<TDep>) this.dependenciesMetaMap.get(dependencyClass).getEntityValues();
    }

    private void doGenerate() {
        List<T> entities = null;
        try {
            if (log.isDebugEnabled())
                log.debug("'{}': Generating entity", entity.getEntityClass());
            entities = this.getEntities();
        } catch (Exception ex) {
            log.error("'{}': Failed to generate entities", entity.getEntityClass(), ex);
            return;
        }
        if (entities == null || entities.isEmpty()) {
            return;
        }

        entities.forEach(entity -> {
            if (entity instanceof NumericallyIdentifiableEntity) {
                NumericallyIdentifiableEntity numericallyIdentifiableEntity = (NumericallyIdentifiableEntity) entity;
                if (numericallyIdentifiableEntity.getId() == null) {
                    int id = getEntityId();
                    if (log.isDebugEnabled())
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
