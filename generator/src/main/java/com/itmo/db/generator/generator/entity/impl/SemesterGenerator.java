package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Semester;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class SemesterGenerator extends AbstractEntityGenerator<Semester, Integer> {

    public SemesterGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Semester.class, deps, generator);
    }

    @Override
    protected List<Semester> getEntities() {
        log.debug("Creating Semester");
        return List.of(new Semester(null));
    }
}

