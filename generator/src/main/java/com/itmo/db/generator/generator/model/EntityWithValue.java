package com.itmo.db.generator.generator.model;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EntityWithValue<T extends (AbstractEntity | List<?>)> {
    public Class<? extends AbstractEntity> entityClass;
    public T entityValue;
}
