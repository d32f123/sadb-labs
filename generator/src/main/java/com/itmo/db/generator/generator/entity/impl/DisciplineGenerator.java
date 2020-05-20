package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Discipline;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class DisciplineGenerator extends AbstractEntityGenerator<Discipline, Integer> {

    public DisciplineGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Discipline.class, deps, generator);
    }

    @Override
    protected Discipline getEntity() {
        log.debug("Creating Discipline");

        return new Discipline(null, "name", "control_form", 17, 17, 58);
    }
}
