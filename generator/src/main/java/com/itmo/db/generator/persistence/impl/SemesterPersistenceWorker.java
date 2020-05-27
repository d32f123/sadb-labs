package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Semester;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.SemesterPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.SemesterPostgresRepository;

import java.util.List;

public class SemesterPersistenceWorker extends AbstractPersistenceWorker<Semester, Integer> {

    private final SemesterPostgresRepository repository;

    public SemesterPersistenceWorker(Generator generator, SemesterPostgresRepository repository) {
        super(Semester.class, generator);
        this.repository = repository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Semester entity) {
        SemesterPostgresDAO semesterPostgresDAO = new SemesterPostgresDAO(null);

        this.repository.save(semesterPostgresDAO);
        return List.of(semesterPostgresDAO);
    }

    @Override
    protected void doCommit() {
        this.repository.flush();
    }
}
