package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Project;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.ProjectMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.ProjectMySQLRepository;

import java.util.Collections;
import java.util.List;

public class ProjectPersistenceWorker extends AbstractPersistenceWorker<Project, Integer> {

    private final ProjectMySQLRepository projectMySQLRepository;

    public ProjectPersistenceWorker(Generator generator, ProjectMySQLRepository projectMySQLRepository) {
        super(Project.class, generator);
        this.projectMySQLRepository = projectMySQLRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Project entity) {
        ProjectMySQLDAO projectMySQLDAO = new ProjectMySQLDAO(
                null,
                entity.getName()
        );

        this.projectMySQLRepository.save(projectMySQLDAO);
        return Collections.singletonList(projectMySQLDAO);
    }

    @Override
    protected void doCommit() {
        this.projectMySQLRepository.flush();
    }
}
