package com.itmo.db.generator.persistence;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.pool.EntityPoolInstance;
import com.itmo.db.generator.utils.eventbus.EventBus;
import com.itmo.db.generator.utils.eventbus.GeneratorEvent;
import com.itmo.db.generator.utils.eventbus.GeneratorEventMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

// TODO: When entity is persisted, return ID which should be set to base entity
@Slf4j
public abstract class AbstractPersistenceWorker<T extends AbstractEntity<TId>, TId> implements PersistenceWorker {

    protected final Generator generator;
    private final Class<T> entityClass;
    private final EntityPoolInstance<T, TId> pool;
    private final EventBus eventBus;
    private boolean shouldContinue;

    public AbstractPersistenceWorker(Class<T> entityClass,
                                     Generator generator) {
        this.generator = generator;
        this.entityClass = entityClass;
        this.eventBus = EventBus.getInstance();
        this.pool = this.generator.getEntityPool(entityClass).getInstance(entityClass);
    }

    // Return map of DAOs
    protected abstract List<? extends IdentifiableDAO<?>> doPersist(T entity);

    protected abstract void doCommit();

    @Override
    public void run() {
        this.shouldContinue = true;
        synchronized (this) {
            if (log.isInfoEnabled())
                log.info("'{}': PersistenceWorker startup", entityClass);
            while (this.shouldContinue) {
                if (log.isDebugEnabled())
                    log.debug("'{}': PersistenceWorker requesting entity", entityClass);
                if (log.isInfoEnabled() && pool.getNumberOfAvailableEntities() % 100 == 0) {
                    log.info("'{}': {} entities left", entityClass, pool.getNumberOfAvailableEntities());
                }
                this.pool.request(1, (entities) -> {
                    if (entities == null || entities.isEmpty()) {
                        if (log.isDebugEnabled())
                            log.debug("'{}': PersistenceWorker got null, notifying main thead that should exit", entityClass);
                        synchronized (this) {
                            this.shouldContinue = false;
                            this.notify();
                        }
                        return;
                    }
                    T entity = entities.get(0);
                    if (log.isDebugEnabled())
                        log.debug("PersistenceWorker '{}' got entity '{}', persisting", entityClass, entity);
                    this.persistEntity(entity);
                    synchronized (this) {
                        this.notify();
                    }
                });

                try {
                    this.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
        this.commit();
    }

    protected <TEntity extends AbstractEntity<TEntityId>, TEntityId,
            TDAO extends IdentifiableDAO<TDAOId>, TDAOId>
    TDAOId getDependencyDAOId(Class<TEntity> entityClass, TEntityId entityId, Class<TDAO> daoClass) {
        return this.generator.getEntityToDAOMapper(entityClass).getDAOId(entityId, daoClass);
    }

    private void persistEntity(T entity) {
        if (log.isDebugEnabled())
            log.debug("'{}': PersistenceWorker persisting", entityClass);
        List<? extends IdentifiableDAO<?>> daoValues = null;
        try {
            daoValues = this.doPersist(entity);
        } catch (Exception ex) {
            log.error("'{}': Error during persistence", entityClass, ex);
        }
        if (daoValues == null) {
            daoValues = Collections.emptyList();
        }

        Map<Class<? extends IdentifiableDAO<?>>, Object> daoMap = new HashMap<>();
        daoValues.stream()
                .filter(Objects::nonNull)
                .filter(value -> value.getId() != null)
                .forEach(value -> daoMap.put((Class) value.getClass(), value.getId()));

        this.eventBus.notify(GeneratorEvent.ENTITY_PERSISTED, new GeneratorEventMessage<>(
                this.entityClass,
                new EntityPersistedEventMessage<>(entity.getId(), daoMap)
        ));
    }

    private void commit() {
        if (log.isInfoEnabled())
            log.info("'{}': PersistenceWorker commiting", entityClass);
        this.doCommit();
        this.eventBus.notify(GeneratorEvent.PERSISTENCE_FINISHED, new GeneratorEventMessage<>(entityClass, null));
    }
}
