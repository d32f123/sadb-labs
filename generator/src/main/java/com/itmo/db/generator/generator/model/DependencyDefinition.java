package com.itmo.db.generator.generator.model;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
public class DependencyDefinition implements Serializable {
    private Class<? extends AbstractEntity> dependencyClass;
    private int amountPerInstance;
}
