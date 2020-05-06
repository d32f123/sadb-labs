package com.itmo.db.generator.pool;

import com.itmo.db.generator.model.entity.AbstractEntity;

import java.util.List;
import java.util.function.Consumer;

public interface EntityPoolInstance<T extends AbstractEntity> {

    void request(int entitiesCount, Consumer<List<T>> callback);

}
