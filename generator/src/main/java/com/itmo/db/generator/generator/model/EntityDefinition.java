package com.itmo.db.generator.generator.model;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
public class EntityDefinition<T extends AbstractEntity<TId>, TId> implements Serializable {
    private Class<T> entityClass;
    private Integer amount;
    private Set<DependencyDefinition<?, ?>> dependencies;
}
