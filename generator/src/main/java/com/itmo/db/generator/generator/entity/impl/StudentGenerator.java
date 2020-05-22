package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Student;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class StudentGenerator extends AbstractEntityGenerator<Student, Integer> {

    public StudentGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Student.class, deps, generator);
    }

    @Override
    protected List<Student> getEntities() {
        log.debug("Creating Student");
        return null;
    }
}

