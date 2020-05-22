package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.StudentSemesterDiscipline;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class StudentSemesterDisciplinePersistenceWorker
        extends AbstractPersistenceWorker<StudentSemesterDiscipline, StudentSemesterDiscipline.StudentSemesterDisciplinePK> {

    public StudentSemesterDisciplinePersistenceWorker(Generator generator) {
        super(StudentSemesterDiscipline.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(StudentSemesterDiscipline entity) {
        return null;
    }

    @Override
    protected void doCommit() {
    }
}
