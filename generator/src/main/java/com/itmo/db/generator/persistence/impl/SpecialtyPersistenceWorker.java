package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.*;
import com.itmo.db.generator.persistence.db.postgres.repository.*;

import java.util.List;

public class SpecialtyPersistenceWorker extends AbstractPersistenceWorker<Specialty, Integer> {

    private final SpecialtyPostgresRepository repository;

    public SpecialtyPersistenceWorker(Generator generator, SpecialtyPostgresRepository repository) {
        super(Specialty.class, generator);
        this.repository = repository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Specialty entity) {
        FacultyPostgresDAO facultyPostgresDAO = new FacultyPostgresDAO(
                this.getDependencyDAOId(Faculty.class, entity.getFacultyId(), FacultyPostgresDAO.class), null, null
        );

        SpecialtyPostgresDAO specialtyPostgresDAO = new SpecialtyPostgresDAO(
                null, entity.getName(), entity.getStudyStandard(), facultyPostgresDAO
        );

        this.repository.save(specialtyPostgresDAO);
        return List.of(specialtyPostgresDAO);
    }

    @Override
    protected void doCommit() {
        this.repository.flush();
    }
}
