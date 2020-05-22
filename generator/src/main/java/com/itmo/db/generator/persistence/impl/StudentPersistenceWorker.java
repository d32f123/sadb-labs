package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Student;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class StudentPersistenceWorker extends AbstractPersistenceWorker<Student, Integer> {

    public StudentPersistenceWorker(Generator generator) {
        super(Student.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Student entity) {
        return null;
    }

    @Override
    protected void doCommit() {
    }
}

