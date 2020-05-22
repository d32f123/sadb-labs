package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Professor;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class ProfessorPersistenceWorker extends AbstractPersistenceWorker<Professor, Integer> {

    public ProfessorPersistenceWorker(Generator generator) {
        super(Professor.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Professor entity) {
        return null;
    }

    @Override
    protected void doCommit() {
    }
}
