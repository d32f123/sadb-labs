package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Project;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.Collections;
import java.util.List;

public class ProjectPersistenceWorker extends AbstractPersistenceWorker<Project, Integer> {

    public ProjectPersistenceWorker(Generator generator) {
        super(Project.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Project entity) {
        return Collections.emptyList();
    }

    @Override
    protected void doCommit() {

    }
}
