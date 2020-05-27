package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.*;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.*;
import com.itmo.db.generator.persistence.db.postgres.repository.*;

import java.util.List;

public class SpecialtyDisciplineLinkPersistenceWorker
        extends AbstractPersistenceWorker<SpecialtyDisciplineLink, SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK> {

    private final SpecialtyDisciplineLinkPostgresRepository repository;

    public SpecialtyDisciplineLinkPersistenceWorker(Generator generator, SpecialtyDisciplineLinkPostgresRepository repository) {
        super(SpecialtyDisciplineLink.class, generator);
        this.repository = repository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(SpecialtyDisciplineLink entity) {
        SpecialtyPostgresDAO specialtyPostgresDAO = new SpecialtyPostgresDAO(this.getDependencyDAOId(Specialty.class, entity.getId().getSpecialtyId(), SpecialtyPostgresDAO.class), null, null, null);
        DisciplinePostgresDAO disciplinePostgresDAO = new DisciplinePostgresDAO(this.getDependencyDAOId(Discipline.class, entity.getId().getDisciplineId(), DisciplinePostgresDAO.class), null, null, null, null, null);

        SpecialtyDisciplineLinkPostgresDAO specialtyDisciplineLinkPostgresDAO = new SpecialtyDisciplineLinkPostgresDAO(
                specialtyPostgresDAO, disciplinePostgresDAO
        );

        this.repository.save(specialtyDisciplineLinkPostgresDAO);
        return List.of(specialtyDisciplineLinkPostgresDAO);
    }

    @Override
    protected void doCommit() {
        this.repository.flush();
    }
}
