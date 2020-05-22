package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Dormitory;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class DormitoryPersistenceWorker extends AbstractPersistenceWorker<Dormitory, Integer> {

    public DormitoryPersistenceWorker(Generator generator) {
        super(Dormitory.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Dormitory entity) {
        return null;
    }

    @Override
    protected void doCommit() {
    }
}
