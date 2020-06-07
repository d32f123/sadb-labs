package com.itmo.db.generator.persistence.impl.itmo;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Group;

public class ItmoGroupPersistenceWorker extends ItmoEntityAbstractPersistenceWorker<Group, Integer> {

    public ItmoGroupPersistenceWorker(Class<Group> entityClass, Generator generator) {
        super(entityClass, generator);
    }
}
