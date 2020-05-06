package com.itmo.db.generator.generator.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.model.EntityWithAmount;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.model.link.AbstractLink;
import com.itmo.db.generator.pool.EntityPool;
import com.itmo.db.generator.pool.EntityPoolInstance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractLinkGenerator<T extends AbstractLink> implements LinkGenerator {

    protected final Generator generator;
    private final Class<T> linkClass;
    private final List<Class<? extends AbstractEntity>> entitiesToWatch;
    private final Random random;

    protected final Map<Class<? extends AbstractEntity>, EntityWithMeta> entitiesMap;
    private int entitiesGenerated;

    // TODO: Lift off here
    @Data
    @AllArgsConstructor
    protected static class EntityWithMeta {
        public Class<? extends AbstractEntity> entityClass;
        public List<? extends AbstractEntity> entityValues;
        public EntityPoolInstance<? extends AbstractEntity> pool;
        public int amount;
    }

    public AbstractLinkGenerator(Class<T> linkClass,
                                 List<Class<? extends AbstractEntity>> entitiesToWatch,
                                 Generator generator) {
        this.generator = generator;
        this.linkClass = linkClass;
        this.entitiesToWatch = entitiesToWatch;
        this.random = new Random(42);
        this.entitiesMap = new HashMap<>(this.entitiesToWatch.size());
        this.entitiesGenerated = 0;
    }

    @Override
    public void run() {
        this.entitiesMap.clear();
        this.entitiesGenerated = 0;

        entitiesToWatch.forEach(entityClass -> {
            int amount = random.nextInt(4);

            EntityWithMeta entityWithMeta = new EntityWithMeta(
                    entityClass,
                    null,
                    this.generator.getEntityPool(entityClass).getInstance(linkClass),
                    amount
            );

            this.entitiesMap.put(entityClass, entityWithMeta);
            entityWithMeta.pool.request(entityWithMeta.amount, (entities) -> {
                entityWithMeta.setEntityValues(entities);
                this.entitiesGenerated += 1;
                this.checkIfConsumersAreDone();
            }));
        });
    }


    private void checkIfConsumersAreDone() {
        if (this.entitiesGenerated < this.entitiesToWatch.size()) {
            return;
        }

        this.
    }

    protected abstract T getLink(List<>);

}
