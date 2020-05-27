package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.*;
import com.itmo.db.generator.persistence.db.postgres.repository.StudentPostgresRepository;

import java.util.List;

public class StudentPersistenceWorker extends AbstractPersistenceWorker<Student, Integer> {

    private final StudentPostgresRepository repository;

    public StudentPersistenceWorker(Generator generator, StudentPostgresRepository repository) {
        super(Student.class, generator);
        this.repository = repository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Student entity) {
        StudentPostgresDAO studentPostgresDAO = new StudentPostgresDAO(
                this.getDependencyDAOId(Person.class, entity.getId(), PersonPostgresDAO.class),
                this.getDependencyDAOId(Specialty.class, entity.getSpecialtyId(), SpecialtyPostgresDAO.class),
                entity.getStudyType()
        );

        this.repository.save(studentPostgresDAO);
        return List.of(studentPostgresDAO);
    }

    @Override
    protected void doCommit() {
        this.repository.flush();
    }
}

