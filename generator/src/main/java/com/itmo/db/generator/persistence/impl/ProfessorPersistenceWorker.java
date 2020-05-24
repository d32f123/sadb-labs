package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.*;
import com.itmo.db.generator.persistence.db.postgres.repository.*;

import java.util.*;

public class ProfessorPersistenceWorker extends AbstractPersistenceWorker<Professor, Integer> {
    private final ProfessorPostgresRepository professorPostgresRepository;

    public ProfessorPersistenceWorker(Generator generator, ProfessorPostgresRepository professorPostgresRepository) {
        super(Professor.class, generator);
        this.professorPostgresRepository = professorPostgresRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Professor entity) {
        PersonPostgresDAO personPostgresDAO = new PersonPostgresDAO(
                this.getDependencyDAOId(Person.class, entity.getId(), PersonPostgresDAO.class), null, null, null, null
        );
        FacultyPostgresDAO facultyPostgresDAO = new FacultyPostgresDAO(
                this.getDependencyDAOId(Faculty.class, entity.getFacultyId(), FacultyPostgresDAO.class), null, null
        );
        ProfessorPostgresDAO professorPostgresDAO = new ProfessorPostgresDAO(
                personPostgresDAO.getId(), personPostgresDAO, facultyPostgresDAO
        );

        this.professorPostgresRepository.save(professorPostgresDAO);
        return List.of(professorPostgresDAO);
    }

    @Override
    protected void doCommit() {
        this.professorPostgresRepository.flush();
    }
}
