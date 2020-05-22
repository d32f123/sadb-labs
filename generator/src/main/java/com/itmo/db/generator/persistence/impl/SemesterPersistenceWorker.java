package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Semester;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class SemesterPersistenceWorker extends AbstractPersistenceWorker<Semester, Integer> {

    public SemesterPersistenceWorker(Generator generator) {
        super(Semester.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Semester entity) {
        return null;
    }

    @Override
    protected void doCommit() {
    }
}
