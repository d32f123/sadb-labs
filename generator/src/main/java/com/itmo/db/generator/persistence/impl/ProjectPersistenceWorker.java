package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Project;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.ProjectDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.ProjectRepository;

import java.util.Collections;
import java.util.List;

public class ProjectPersistenceWorker extends AbstractPersistenceWorker<Project, Integer> {

    private final ProjectRepository projectRepository;

    public ProjectPersistenceWorker(Generator generator, ProjectRepository projectRepository) {
        super(Project.class, generator);
        this.projectRepository = projectRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Project entity) {
        ProjectDAO projectDAO = new ProjectDAO(
                entity.getId().longValue(),
                entity.getName()
        );

        this.projectRepository.save(projectDAO);
        return Collections.singletonList(projectDAO);
    }

    @Override
    protected void doCommit() {
        this.projectRepository.flush();
    }
}
