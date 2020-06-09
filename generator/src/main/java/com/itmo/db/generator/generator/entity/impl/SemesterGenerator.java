package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Semester;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SemesterGenerator extends AbstractEntityGenerator<Semester, Integer> {

    public SemesterGenerator(EntityDefinition<Semester, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    @Override
    protected List<Semester> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Creating Semester");
        return List.of(new Semester(null));
    }
}

