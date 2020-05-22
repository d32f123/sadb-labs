package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Specialty;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class SpecialtyGenerator extends AbstractEntityGenerator<Specialty, Integer> {

    public SpecialtyGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Specialty.class, deps, generator);
    }

    @Override
    protected List<Specialty> getEntities() {
        log.debug("Creating Specialty");
        return null;
    }
}

