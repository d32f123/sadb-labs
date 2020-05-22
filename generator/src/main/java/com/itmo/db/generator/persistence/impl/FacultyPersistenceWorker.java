package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.*;
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
        UniversityPostgresDAO universityPostgresDAO = new UniversityPostgresDAO(
                this.getDependencyDAOId(University.class, entity.getUniversityId(), UniversityPostgresDAO.class), null, null
        );

        FacultyPostgresDAO facultyPostgresDAO = new FacultyPostgresDAO(
                null,
                entity.getFacultyName(),
                universityPostgresDAO
        );

        this.facultyPostgresRepository.save(facultyPostgresDAO);
        return Collections.singletonList(facultyPostgresDAO);
    }

    @Override
    protected void doCommit() {

    }


}
