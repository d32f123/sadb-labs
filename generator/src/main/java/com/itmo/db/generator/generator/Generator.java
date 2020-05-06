package com.itmo.db.generator.generator;

import com.itmo.db.generator.eventmanager.EventManager;
import com.itmo.db.generator.generator.entity.EntityGeneratorFactory;
import com.itmo.db.generator.generator.event.GenerationEvent;
import com.itmo.db.generator.generator.model.EntityWithAmount;
import com.itmo.db.generator.generator.model.EntityWithValue;
import com.itmo.db.generator.generator.model.GeneratableEntity;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.model.link.AbstractLink;
import com.itmo.db.generator.pool.EntityPool;
import com.itmo.db.generator.pool.impl.EntityPoolImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class Generator extends EventManager<GenerationEvent, EntityWithValue<? extends AbstractEntity>> {

    private Map<Class<? extends AbstractEntity>, GeneratableEntity<? extends AbstractEntity>> entities;

    // TODO: List of levels, so that we can map entities which are dependant on another entities being created
    public void generate(List<EntityWithAmount> entities, List<Class<? extends AbstractLink>> links) {
        this.entities = new HashMap<>(entities.size());

        entities.stream()
                .map(entity -> new GeneratableEntity<>(
                        entity.entityClass,
                        new EntityPoolImpl<>(entity.amount),
                        EntityGeneratorFactory.getInstance().getGenerator(entity.entityClass, this),
                        false
                ))
                .forEach(entity -> this.entities.put(entity.entityClass, entity));

        this.entities.values().forEach(entity -> {
            entity.pool.subscribe(GenerationEvent.ENTITY_GENERATED, (event, arg) -> {
                this.notify(GenerationEvent.ENTITY_GENERATED, new EntityWithValue<>(entity.entityClass, arg));
            });
            entity.pool.subscribe(GenerationEvent.ENTITY_GENERATION_FINISHED, (event, arg) -> {
                this.entities.get(entity.entityClass).generated = true;
                this.checkIfEntitiesAreDone();
            });
        });
    }

    private synchronized void checkIfEntitiesAreDone() {
        log.info("Checking if done");
        if (entities.values().stream().anyMatch(entity -> !entity.generated)) {
            log.info("Found at least one entity still not generated");
            return;
        }

        log.info("Generated all entities, emitting done");
        this.notify(GenerationEvent.ENTITY_GENERATION_FINISHED, null);
    }

    public <T extends AbstractEntity> EntityPool<T> getEntityPool(Class<T> entityClass) {
        return (EntityPool<T>) entities.get(entityClass).pool;
    }

}
