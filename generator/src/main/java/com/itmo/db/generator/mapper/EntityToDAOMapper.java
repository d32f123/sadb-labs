package com.itmo.db.generator.mapper;

import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.persistence.EntityPersistedEventMessage;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.utils.eventbus.EventBus;
import com.itmo.db.generator.utils.eventbus.GeneratorEvent;
import com.itmo.db.generator.utils.eventbus.GeneratorEventMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
public class EntityToDAOMapper<TEntity extends AbstractEntity<TEntityId>, TEntityId> {

    private final EventBus eventBus;
    private final Class<TEntity> entityClass;
    private final Map<TEntityId, Map<Class<? extends IdentifiableDAO<?>>, ?>> entityToDAOMap = new ConcurrentHashMap<>();

    private Consumer<GeneratorEventMessage<TEntity, TEntityId, TEntity>> entityGeneratedConsumer;
    private Consumer<GeneratorEventMessage<TEntity, TEntityId, EntityPersistedEventMessage<TEntityId>>> entityPersistedConsumer;

    public EntityToDAOMapper(Class<TEntity> entityClass) {
        this.eventBus = EventBus.getInstance();
        this.entityClass = entityClass;
        this.eventBus.subscribe(GeneratorEvent.ENTITY_GENERATED, entityClass, this.getEntityGeneratedConsumer());
        this.eventBus.subscribe(GeneratorEvent.ENTITY_PERSISTED, entityClass, this.getEntityPersistedConsumer());
    }

    public Map<Class<? extends IdentifiableDAO<?>>, ?> getDAOs(TEntityId id) {
        if (!this.entityToDAOMap.containsKey(id)) {
            return Collections.emptyMap();
        }

        return this.entityToDAOMap.get(id);
    }

    public <TDAO extends IdentifiableDAO<TDAOId>, TDAOId> TDAOId getDAOId(TEntityId id, Class<TDAO> daoClass) {
        if (log.isTraceEnabled()) {
            log.trace("Getting entity '{}' by id '{}' and class '{}'", entityClass, id, daoClass);
        }
        return (TDAOId) this.entityToDAOMap.get(id).get(daoClass);
    }

    private Consumer<GeneratorEventMessage<TEntity, TEntityId, TEntity>> getEntityGeneratedConsumer() {
        if (this.entityGeneratedConsumer != null) {
            return this.entityGeneratedConsumer;
        }

        this.entityGeneratedConsumer = (message) -> {
            if (log.isDebugEnabled())
                log.debug("Mapper '{}' got entity generated with id: '{}'", entityClass, message.getMessage().getId());
            this.entityToDAOMap.put(message.getMessage().getId(), new HashMap<>());
        };
        return this.entityGeneratedConsumer;
    }

    private Consumer<GeneratorEventMessage<TEntity, TEntityId, EntityPersistedEventMessage<TEntityId>>>
    getEntityPersistedConsumer() {
        if (this.entityPersistedConsumer != null) {
            return this.entityPersistedConsumer;
        }

        this.entityPersistedConsumer = (message) -> {
            if (log.isDebugEnabled())
                log.debug("Mapper '{}' got entity persisted with id: '{}'. Map: '{}'",
                        entityClass, message.getMessage().getEntityId(), message.getMessage().getDaoValuesMap());
            this.entityToDAOMap.put(
                    message.getMessage().getEntityId(),
                    message.getMessage().getDaoValuesMap() != null
                            ? Map.copyOf(message.getMessage().getDaoValuesMap())
                            : Collections.emptyMap()
            );
        };
        return this.entityPersistedConsumer;
    }

}
