package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.StudentSemesterDiscipline;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class StudentSemesterDisciplineGenerator
        extends AbstractEntityGenerator<StudentSemesterDiscipline, StudentSemesterDiscipline.StudentSemesterDisciplinePK> {

    public StudentSemesterDisciplineGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(StudentSemesterDiscipline.class, deps, generator);
    }

    @Override
    protected List<StudentSemesterDiscipline> getEntities() {
        log.debug("Creating StudentSemesterDiscipline");
        return null;
    }
}

