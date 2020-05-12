package com.itmo.db.generator.persistence;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.pool.EntityPool;
import com.itmo.db.generator.utils.eventbus.EventBus;
import com.itmo.db.generator.utils.eventbus.GeneratorEvent;
import com.itmo.db.generator.utils.eventbus.GeneratorEventMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public abstract class AbstractPersistenceWorker<T extends AbstractEntity> implements PersistenceWorker {

    protected final Generator generator;
    private final Class<T> entityClass;
    protected EntityPool<T> pool;
    private final EventBus eventBus;

    private Consumer<GeneratorEventMessage<T>> entityGeneratedConsumer;
    private Consumer<GeneratorEventMessage<T>> entityGenerationFinishedConsumer;

    public AbstractPersistenceWorker(Class<T> entityClass,
                                     Generator generator) {
        this.generator = generator;
        this.entityClass = entityClass;
        this.eventBus = EventBus.getInstance();
        this.eventBus.subscribe(GeneratorEvent.ENTITY_GENERATED, entityClass, this.getEntityGeneratedConsumer());
        this.eventBus.subscribe(GeneratorEvent.ENTITY_GENERATION_FINISHED, entityClass, this.getEntityGenerationFinishedConsumer());
        this.pool = this.generator.getEntityPool(entityClass);
    }

    protected abstract void doPersist(T entity);
    protected abstract void doCommit();

    @Override
    public void run() {
    }

    private Consumer<GeneratorEventMessage<T>> getEntityGeneratedConsumer() {
        if (this.entityGeneratedConsumer != null) {
            return this.entityGeneratedConsumer;
        }

        this.entityGeneratedConsumer = (message) -> {
            log.info("'{}': ENTITY GENERATED message received, calling doPersist", entityClass);
            this.doPersist(message.getMessage());
        };
        return this.entityGeneratedConsumer;
    }

    private Consumer<GeneratorEventMessage<T>> getEntityGenerationFinishedConsumer() {
        if (this.entityGenerationFinishedConsumer != null) {
            return this.entityGenerationFinishedConsumer;
        }

        this.entityGenerationFinishedConsumer = (message) -> {
            log.info("'{}': ENTITY GENERATION FINISHED message received, calling doCommit and unsubbing", entityClass);
            this.eventBus.unsubscribe(GeneratorEvent.ENTITY_GENERATED, entityClass, this.getEntityGeneratedConsumer());
            this.eventBus.unsubscribe(GeneratorEvent.ENTITY_GENERATION_FINISHED, entityClass, this.getEntityGenerationFinishedConsumer());
            log.info("'{}': Successfully unsubbed", entityClass);
            this.doCommit();
            this.eventBus.notify(GeneratorEvent.PERSISTENCE_FINISHED, new GeneratorEventMessage<>(entityClass, null));
        };
        return this.entityGenerationFinishedConsumer;
    }
}
