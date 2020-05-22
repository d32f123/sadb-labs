package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class ProfessorGenerator extends AbstractEntityGenerator<Professor, Integer> {

    public ProfessorGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Professor.class, deps, generator);
    }

    @Override
    protected List<Professor> getEntities() {
        log.debug("Creating Professor");

        Person person = this.getDependencyInstances(Person.class).get(0);
        Faculty faculty = this.getDependencyInstances(Faculty.class).get(0);

        return List.of(new Professor(person.getId(), faculty.getId()));
    }
}

