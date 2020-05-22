package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class StudentGenerator extends AbstractEntityGenerator<Student, Integer> {

    public String getStudyType(Random random) {
        String[] types = new String[] {"очная", "заочная"};
        return types[random.nextInt(types.length)];
    }

    public StudentGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Student.class, deps, generator);
    }

    @Override
    protected List<Student> getEntities() {
        log.debug("Creating Student");

        Person person = this.getDependencyInstances(Person.class).get(0);
        Specialty specialty = this.getDependencyInstances(Specialty.class).get(0);

        return List.of(new Student(person.getId(), specialty.getId(), getStudyType(new Random())));
    }
}

