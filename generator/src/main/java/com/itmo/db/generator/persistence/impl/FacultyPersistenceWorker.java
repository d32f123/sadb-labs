package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Faculty;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.FacultyDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.FacultyRepository;

import java.util.Collections;
import java.util.List;

public class FacultyPersistenceWorker extends AbstractPersistenceWorker<Faculty, Integer> {

    private final FacultyRepository facultyRepository;

    public FacultyPersistenceWorker(Generator generator, FacultyRepository facultyRepository) {
        super(Faculty.class, generator);
        this.facultyRepository = facultyRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Faculty entity) {
        FacultyDAO facultyDAO = new FacultyDAO(
                null,
                entity.getFacultyName()
        );

        this.facultyRepository.save(facultyDAO);
        return Collections.singletonList(facultyDAO);
    }

    @Override
    protected void doCommit() {

    }


}
