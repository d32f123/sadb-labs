package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Project;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;

public class ProjectPersistenceWorker extends AbstractPersistenceWorker<Project> {

    public ProjectPersistenceWorker(Generator generator) {
        super(Project.class, generator);
    }

    @Override
    protected void doPersist(Project entity) {

    }

    @Override
    protected void doCommit() {

    }
}
