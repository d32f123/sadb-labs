package com.itmo.db.generator.generator.model;

import com.itmo.db.generator.generator.entity.EntityGenerator;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.pool.EntityPool;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneratableEntity<T extends AbstractEntity> {
    public Class<? extends AbstractEntity> entityClass;
    public EntityPool<T> pool;
    public EntityGenerator generator;
    public boolean generated;
}
