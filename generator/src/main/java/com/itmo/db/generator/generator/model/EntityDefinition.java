package com.itmo.db.generator.generator.model;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
public class EntityDefinition implements Serializable {
    private Class<? extends AbstractEntity> entityClass;
    private int amount;
    private Set<DependencyDefinition> dependencies;
}
