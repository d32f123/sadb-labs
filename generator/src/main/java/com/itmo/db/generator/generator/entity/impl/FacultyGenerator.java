package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Faculty;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class FacultyGenerator extends AbstractEntityGenerator<Faculty, Integer> {
    
    public FacultyGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Faculty.class, deps, generator);
    }

    @Override
    protected Faculty getEntity() {
        log.debug("Creating Faculty");
        Faculty faculty = new Faculty(null, "wtf");
        // Data filling logic goes here
        return faculty;
    }

}
