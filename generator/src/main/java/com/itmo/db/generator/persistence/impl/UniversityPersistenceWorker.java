package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.University;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.UniversityPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.UniversityPostgresRepository;

import java.util.Collections;
import java.util.List;

public class UniversityPersistenceWorker extends AbstractPersistenceWorker<University, Integer> {

    private final UniversityPostgresRepository universityPostgresRepository;

    public UniversityPersistenceWorker(Generator generator, UniversityPostgresRepository universityPostgresRepository) {
        super(University.class, generator);
        this.universityPostgresRepository = universityPostgresRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(University entity) {
        UniversityPostgresDAO universityPostgresDAO = new UniversityPostgresDAO(
                null,
                entity.getName(),
                entity.getCreationDate()
        );

        this.universityPostgresRepository.save(universityPostgresDAO);
        return Collections.singletonList(universityPostgresDAO);
    }

    @Override
    protected void doCommit() {
        this.universityPostgresRepository.flush();
    }
}
