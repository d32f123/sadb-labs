package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Discipline;
import com.itmo.db.generator.model.entity.Specialty;
import com.itmo.db.generator.model.entity.link.SpecialtyDisciplineLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.DisciplinePostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.SpecialtyDisciplineLinkPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.SpecialtyPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.SpecialtyDisciplineLinkPostgresRepository;

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
        SpecialtyDisciplineLinkPostgresDAO specialtyDisciplineLinkPostgresDAO = new SpecialtyDisciplineLinkPostgresDAO(
                this.getDependencyDAOId(Specialty.class, entity.getId().getSpecialtyId(), SpecialtyPostgresDAO.class),
                this.getDependencyDAOId(Discipline.class, entity.getId().getDisciplineId(), DisciplinePostgresDAO.class)
        );

        this.repository.save(specialtyDisciplineLinkPostgresDAO);
        return List.of(specialtyDisciplineLinkPostgresDAO);
    }

    @Override
    protected void doCommit() {
        this.repository.flush();
    }
}
