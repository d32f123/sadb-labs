package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Faculty;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.postgres.dao.FacultyDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.FacultyRepository;

public class FacultyPersistenceWorker extends AbstractPersistenceWorker<Faculty> {

    private final FacultyRepository facultyRepository;

    public FacultyPersistenceWorker(Generator generator, FacultyRepository facultyRepository) {
        super(Faculty.class, generator);
        this.facultyRepository = facultyRepository;
    }

    @Override
    protected void doPersist(Faculty entity) {
        FacultyDAO personDAO = new FacultyDAO(
                entity.getId(),
                entity.getFacultyName()
        );

        this.facultyRepository.save(personDAO);
    }

    @Override
    protected void doCommit() {

    }


}
