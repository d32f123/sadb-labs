package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Faculty;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Professor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public class ProfessorGenerator extends AbstractEntityGenerator<Professor, Integer> {

    public ProfessorGenerator(EntityDefinition<Professor, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    @Override
    protected List<Professor> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Creating Professor");

        Person person = this.getDependencyInstances(Person.class).get(0);
        if (!person.getRole().equals("docent")) {
            return Collections.emptyList();
        }
        Faculty faculty = this.getDependencyInstances(Faculty.class).get(0);

        return List.of(new Professor(person.getId(), faculty.getId(), person.getPersonNumber()));
    }
}

