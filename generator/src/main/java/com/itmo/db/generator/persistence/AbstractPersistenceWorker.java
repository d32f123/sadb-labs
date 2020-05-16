package com.itmo.db.generator.persistence;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.mapper.EntityToDAOMapper;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.pool.EntityPoolInstance;
import com.itmo.db.generator.utils.eventbus.EventBus;
import com.itmo.db.generator.utils.eventbus.GeneratorEvent;
import com.itmo.db.generator.utils.eventbus.GeneratorEventMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: When entity is persisted, return ID which should be set to base entity
@Slf4j
public abstract class AbstractPersistenceWorker<T extends AbstractEntity<TId>, TId> implements PersistenceWorker {

    protected final Generator generator;
    private final Class<T> entityClass;
    private final EntityPoolInstance<T, TId> pool;
    protected final EntityToDAOMapper<T, TId> mapper;
    private final EventBus eventBus;
    private boolean shouldContinue;

    public AbstractPersistenceWorker(Class<T> entityClass,
                                     Generator generator) {
        this.generator = generator;
        this.entityClass = entityClass;
        this.eventBus = EventBus.getInstance();
        this.pool = this.generator.getEntityPool(entityClass).getInstance(entityClass);
        this.mapper = this.generator.getEntityToDAOMapper(entityClass);
    }

    // Return map of DAOs
    protected abstract List<? extends IdentifiableDAO<?>> doPersist(T entity);
    protected abstract void doCommit();

    @Override
    public void run() {
        this.shouldContinue = true;
        while (this.shouldContinue) {
            log.debug("PersistenceWorker '{}' requesting entity", entityClass);
            this.pool.request(1, (entities) -> {
                if (entities == null || entities.isEmpty()) {
                    log.debug("PersistenceWorker '{}' got null, notifying main thead that should exit", entityClass);
                    this.shouldContinue = false;
                    synchronized (this) {
                        this.notify();
                    }
                    return;
                }
                log.debug("PersistenceWorker '{}' got entity '{}', persisting", entityClass, entities.get(0));
                this.persistEntity(entities.get(0));
                synchronized (this) {
                    this.notify();
                }
            });
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException ignored) {}
            }
        }
        this.commit();
    }

    private void persistEntity(T entity) {
        log.info("'{}': ENTITY GENERATED message received, calling doPersist", entityClass);
        List<? extends IdentifiableDAO<?>> daoValues = this.doPersist(entity);
        Map<Class<? extends IdentifiableDAO<?>>, Object> daoMap = new HashMap<>();
        daoValues.forEach(value -> daoMap.put((Class) value.getClass(), value.getId()));

        this.eventBus.notify(GeneratorEvent.ENTITY_PERSISTED, new GeneratorEventMessage<>(
                this.entityClass,
                new EntityPersistedEventMessage<>(entity.getId(), daoMap)
        ));
    }

    private void commit() {
        log.info("'{}': ENTITY GENERATION FINISHED message received, calling doCommit", entityClass);
        this.doCommit();
        this.eventBus.notify(GeneratorEvent.PERSISTENCE_FINISHED, new GeneratorEventMessage<>(entityClass, null));
    }
}
