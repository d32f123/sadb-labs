package com.itmo.db.generator.generator.model;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
public class DependencyDefinition<T extends AbstractEntity<TId>, TId> implements Serializable {
    private Class<T> dependencyClass;
    private int amountPerInstance;
}
