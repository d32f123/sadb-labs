package com.itmo.db.generator.generator;

import com.itmo.db.generator.generator.entity.EntityGeneratorFactory;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.generator.model.GeneratableEntity;
import com.itmo.db.generator.mapper.EntityToDAOMapper;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.persistence.PersistenceWorkerFactory;
import com.itmo.db.generator.pool.EntityPool;
import com.itmo.db.generator.pool.ThreadPoolFactory;
import com.itmo.db.generator.pool.impl.EntityPoolImpl;
import com.itmo.db.generator.utils.dependencytree.DependencyTree;
import com.itmo.db.generator.utils.eventbus.EventBus;
import com.itmo.db.generator.utils.eventbus.GeneratorEvent;
import com.itmo.db.generator.utils.eventbus.GeneratorEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// For each entity generated, ENTITY_GENERATED event is fired
//  Persister persists entities by entity_generated event
// When ENTITI_GENERATION_FINISHEd event is fired,
//  Persister commits the transaction and emits PERSISTANCE_FINISHED_EVENT
// Main Generator then checks if level generation is done

@Component
@Slf4j
public class Generator {
    private DependencyTree dependencyTree;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private PersistenceWorkerFactory persistenceWorkerFactory;

    @Autowired
    private EntityGeneratorFactory entityGeneratorFactory;

    @Autowired
    private ThreadPoolFactory threadPoolFactory;

    private Map<Class<? extends AbstractEntity<?>>, GeneratableEntity<? extends AbstractEntity<?>, ?>> allEntities;
    private Map<Class<? extends AbstractEntity<?>>, GeneratableEntity<? extends AbstractEntity<?>, ?>> currentEntities;
    private int currentLevel;
    private boolean done;
    private final Thread mainThread;

    public Generator() {
        this.mainThread = Thread.currentThread();
    }

    // TODO: List of levels, so that we can map entities which are dependant on another entities being created
    // TODO: Initiate links creation
    // TODO: Check if links are even needed
    public void generate(Set<EntityDefinition<?, ?>> entities) {
        this.dependencyTree = new DependencyTree(entities);

        this.eventBus.subscribe(GeneratorEvent.ENTITY_GENERATION_FINISHED, (message) -> {
            log.debug("Received a ENTITY GENERATION FINISHED event from '{}'", message.getSender());
            synchronized (this.mainThread) {
                log.trace("'{}': Acquired lock and setting generated to true", message.getSender());
                this.currentEntities.get(message.getSender()).setGenerated(true);
            }
        });

        this.eventBus.subscribe(GeneratorEvent.PERSISTENCE_FINISHED, (message) -> {
            log.debug("Received a ENTITY PERSISTENCE FINISHED event from '{}'", message.getSender());
            synchronized (this.mainThread) {
                log.trace("'{}': Acquired lock and setting persisted to true", message.getSender());
                this.currentEntities.get(message.getSender()).setPersisted(true);
                this.checkIfEntitiesAreDone();
            }
        });

        this.eventBus.subscribe(GeneratorEvent.LEVEL_GENERATION_FINISHED, (message) -> {
            log.debug("Received a LEVEL GENERATION FINISHED event");
            if (this.currentLevel == this.dependencyTree.getDependencyLevelsCount() - 1) {
                this.finalizeGeneration();
                return;
            }
            this.currentLevel += 1;
            this.generateCurrentLevelEntities();
        });

        this.currentEntities = new HashMap<>(this.dependencyTree.getDependencyLevel(0).size());
        this.allEntities = new HashMap<>();
        this.currentLevel = 0;
        this.done = false;
        this.generateCurrentLevelEntities();

        // Wait here until workers are done
        synchronized (this.mainThread) {
            while (!this.done) {
                try {
                    Thread.currentThread().wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
        log.info("Shutting down pools");
        this.threadPoolFactory.getPoolInstance().shutdownNow();
        this.eventBus.shutdown();
        log.info("Main thread exiting now");
    }

    private void finalizeGeneration() {
        log.info("We are all done, acquiring lock and notifying main thread");
        synchronized (this.mainThread) {
            log.trace("Acquired lock to finalize");
            this.done = true;
            this.mainThread.notifyAll();
        }
    }

    private void checkIfEntitiesAreDone() {
        log.debug("Checking if done");
        if (currentEntities.values().stream().anyMatch(entity -> !entity.isGenerated() || !entity.isPersisted())) {
            log.trace("Found at least one entity still not generated or persisted");
            return;
        }

        log.trace("Persisted all entities, emitting done");
        // Notify that we are done generating current level
        this.eventBus.notify(GeneratorEvent.LEVEL_GENERATION_FINISHED, new GeneratorEventMessage<>(null, null));
    }

    private void generateCurrentLevelEntities() {
        log.info("Generating level '{}'", this.currentLevel);
        this.currentEntities.clear();
        this.dependencyTree.getDependencyLevel(this.currentLevel)
                .forEach(entity -> {
                    log.trace("Instantiating generatableEntity for entity '{}'", entity);
                    GeneratableEntity<?, ?> generatableEntity = new GeneratableEntity(
                            entity.getEntityClass(),
                            new EntityPoolImpl<>(entity.getEntityClass(), entity.getAmount()),
                            new EntityToDAOMapper<>(entity.getEntityClass())
                    );
                    this.currentEntities.put(entity.getEntityClass(), generatableEntity);
                    this.allEntities.put(entity.getEntityClass(), generatableEntity);
                    this.currentEntities.get(entity.getEntityClass()).setGenerator(
                            this.entityGeneratorFactory.getGenerator(
                                    entity.getEntityClass(),
                                    entity.getDependencies()
                            )
                    );
                    this.currentEntities.get(entity.getEntityClass()).setPersistenceWorker(
                            this.persistenceWorkerFactory.getWorker(
                                    entity.getEntityClass()
                            )
                    );
                });

        log.debug("Level ready to run: '{}'", this.currentEntities.values());
        this.currentEntities.values().forEach(entity -> {
            log.trace("Enqueuing threads for entity '{}'", entity.getEntityClass());
            entity.setGeneratorThread(this.threadPoolFactory.getPoolInstance().submit(entity.getGenerator()));
            entity.setPersistenceWorkerThread(this.threadPoolFactory.getPoolInstance().submit(entity.getPersistenceWorker()));
        });
    }

    public <T extends AbstractEntity<TId>, TId> EntityPool<T, TId> getEntityPool(Class<T> entityClass) {
        return (EntityPool<T, TId>) this.allEntities.get(entityClass).getPool();
    }

    public <T extends AbstractEntity<TId>, TId> EntityToDAOMapper<T, TId> getEntityToDAOMapper(Class<T> entityClass) {
        return (EntityToDAOMapper<T, TId>) this.allEntities.get(entityClass).getMapper();
    }

}
