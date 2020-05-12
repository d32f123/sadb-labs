package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Project;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class ProjectGenerator extends AbstractEntityGenerator<Project> {

    public ProjectGenerator(Set<DependencyDefinition> dependencies, Generator generator) {
        super(Project.class, dependencies, generator);
    }

    @Override
    protected Project getEntity() {
        log.debug("Creating Project");
        Project project = new Project();

        return project;
    }
}
