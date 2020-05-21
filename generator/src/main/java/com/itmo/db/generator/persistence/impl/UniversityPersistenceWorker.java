package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.University;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.UniversityDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.UniversityRepository;

import java.util.*;

public class UniversityPersistenceWorker extends AbstractPersistenceWorker<University, Integer> {

    private final UniversityRepository universityRepository;

    public UniversityPersistenceWorker(Generator generator, UniversityRepository universityRepository) {
        super(University.class, generator);
        this.universityRepository = universityRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(University entity) {
        UniversityDAO universityDAO = new UniversityDAO(
                null,
                entity.getName(),
                entity.getCreationDate()
        );

        this.universityRepository.save(universityDAO);
        return Collections.singletonList(universityDAO);
    }

    @Override
    protected void doCommit() {
        this.universityRepository.flush();
    }
}
