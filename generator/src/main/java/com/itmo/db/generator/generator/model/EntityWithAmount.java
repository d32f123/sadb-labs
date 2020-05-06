package com.itmo.db.generator.generator.model;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntityWithAmount {
    public Class<? extends AbstractEntity> entityClass;
    public int amount;
}
