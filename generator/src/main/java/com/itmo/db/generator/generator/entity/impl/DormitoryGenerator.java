package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Dormitory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class DormitoryGenerator extends AbstractEntityGenerator<Dormitory, Integer> {

    public DormitoryGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Dormitory.class, deps, generator);
    }

    @Override
    protected List<Dormitory> getEntities() {
        log.debug("Creating Dormitory");
        return null;
    }
}

