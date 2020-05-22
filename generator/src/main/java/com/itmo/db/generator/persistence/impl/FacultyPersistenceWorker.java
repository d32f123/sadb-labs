package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Faculty;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.FacultyPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.FacultyPostgresRepository;

import java.util.Collections;
import java.util.List;

public class FacultyPersistenceWorker extends AbstractPersistenceWorker<Faculty, Integer> {

    private final FacultyPostgresRepository facultyPostgresRepository;

    public FacultyPersistenceWorker(Generator generator, FacultyPostgresRepository facultyPostgresRepository) {
        super(Faculty.class, generator);
        this.facultyPostgresRepository = facultyPostgresRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Faculty entity) {
        FacultyPostgresDAO facultyPostgresDAO = new FacultyPostgresDAO(
                null,
                entity.getFacultyName(),
                null
        );

        this.facultyPostgresRepository.save(facultyPostgresDAO);
        return Collections.singletonList(facultyPostgresDAO);
    }

    @Override
    protected void doCommit() {

    }


}
