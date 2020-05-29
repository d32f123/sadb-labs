package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Faculty;
import com.itmo.db.generator.model.entity.Specialty;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.FacultyPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.SpecialtyPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.SpecialtyPostgresRepository;

import java.util.List;

public class SpecialtyPersistenceWorker extends AbstractPersistenceWorker<Specialty, Integer> {

    private final SpecialtyPostgresRepository repository;

    public SpecialtyPersistenceWorker(Generator generator, SpecialtyPostgresRepository repository) {
        super(Specialty.class, generator);
        this.repository = repository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Specialty entity) {
        SpecialtyPostgresDAO specialtyPostgresDAO = new SpecialtyPostgresDAO(
                null,
                entity.getName(),
                entity.getStudyStandard(),
                this.getDependencyDAOId(Faculty.class, entity.getFacultyId(), FacultyPostgresDAO.class)
        );

        this.repository.save(specialtyPostgresDAO);
        return List.of(specialtyPostgresDAO);
    }

    @Override
    protected void doCommit() {
        this.repository.flush();
    }
}
