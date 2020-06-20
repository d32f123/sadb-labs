package com.itmo.db.generator.utils.dependencytree;

import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.AbstractEntity;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_SET;
import static java.util.Collections.emptySet;

public class DependencyTree {
    private final List<Set<EntityDefinition>> leveledEntities;

    public DependencyTree(Set<EntityDefinition<?, ?>> entities) {
        this.leveledEntities = new ArrayList<>();

        Map<Class<? extends AbstractEntity<?>>, EntityDefinition<?, ?>> entitiesMap = new HashMap<>();
        entities.forEach(entity ->
            entitiesMap.put(entity.getEntityClass(), new EntityDefinition<>(
                    entity.getEntityClass(),
                    entity.getAmount(),
                    entity.getDependencies() != null ? new HashSet<>(entity.getDependencies()) : emptySet()
            ))
        );
        this.buildDependencyLevels(entitiesMap);
    }

    public List<Set<EntityDefinition>> getLeveledEntities() {
        return this.leveledEntities;
    }

    public Set<EntityDefinition> getDependencyLevel(int index) {
        return this.leveledEntities.get(index);
    }

    public int getDependencyLevelsCount() {
        return this.leveledEntities.size();
    }

    private void buildDependencyLevels(Map<Class<? extends AbstractEntity<?>>, EntityDefinition<?, ?>> entities) {
        Map<Class<? extends AbstractEntity<?>>, EntityDefinition<?, ?>> modifiableMap = new HashMap<>(entities.size());
        entities.values().forEach(entity ->
            modifiableMap.put(entity.getEntityClass(), new EntityDefinition<>(
                    entity.getEntityClass(),
                    entity.getAmount(),
                    new HashSet<>(entity.getDependencies())
            ))
        );
        this.buildDependencyLevel(modifiableMap, entities);
    }

    private void buildDependencyLevel(Map<Class<? extends AbstractEntity<?>>, EntityDefinition<?, ?>> entities,
                                      Map<Class<? extends AbstractEntity<?>>, EntityDefinition<?, ?>> baseEntities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }
        Set<Class<? extends AbstractEntity<?>>> currentLevel = new HashSet<>(entities.size() / 2);

        // deps
        entities.values().stream()
                .map(entity -> entity.getDependencies() != null
                        ? entity.getDependencies().stream()
                        .map(DependencyDefinition::getDependencyClass)
                        .collect(Collectors.toUnmodifiableSet())
                        : EMPTY_SET

                )
                .filter(deps -> !deps.isEmpty())
                .forEach(currentLevel::addAll);
        // deps + consumers
        currentLevel.addAll(entities.keySet());
        // deps + consumers - consumers w\ deps
        currentLevel.removeAll(
                entities.values().stream()
                        .filter((entity) -> entity.getDependencies() != null
                                && !entity.getDependencies().isEmpty())
                        .map(EntityDefinition::getEntityClass)
                        .collect(Collectors.toUnmodifiableSet())
        );

        // update level list
        this.leveledEntities.add(currentLevel.stream().map(baseEntities::get).collect(Collectors.toUnmodifiableSet()));

        // update map
        currentLevel.forEach(entities::remove);
        // remove dependencies from map
        entities.values().forEach(
                entity -> entity.getDependencies().removeIf(dep -> currentLevel.contains(dep.getDependencyClass()))
        );

        // continue with building next level
        this.buildDependencyLevel(entities, baseEntities);
    }
}
