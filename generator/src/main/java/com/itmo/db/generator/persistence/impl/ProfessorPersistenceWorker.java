package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Faculty;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Professor;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.FacultyPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.PersonPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.ProfessorPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.ProfessorPostgresRepository;

import java.util.List;

public class ProfessorPersistenceWorker extends AbstractPersistenceWorker<Professor, Integer> {
    private final ProfessorPostgresRepository professorPostgresRepository;

    public ProfessorPersistenceWorker(Generator generator, ProfessorPostgresRepository professorPostgresRepository) {
        super(Professor.class, generator);
        this.professorPostgresRepository = professorPostgresRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Professor entity) {
        ProfessorPostgresDAO professorPostgresDAO = new ProfessorPostgresDAO(
                this.getDependencyDAOId(Person.class, entity.getId(), PersonPostgresDAO.class),
                this.getDependencyDAOId(Faculty.class, entity.getFacultyId(), FacultyPostgresDAO.class)
        );

        this.professorPostgresRepository.save(professorPostgresDAO);
        return List.of(professorPostgresDAO);
    }

    @Override
    protected void doCommit() {
        this.professorPostgresRepository.flush();
    }
}
