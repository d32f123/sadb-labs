package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Specialty;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class SpecialtyPersistenceWorker extends AbstractPersistenceWorker<Specialty, Integer> {

    public SpecialtyPersistenceWorker(Generator generator) {
        super(Specialty.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Specialty entity) {
        return null;
    }

    @Override
    protected void doCommit() {
    }
}
