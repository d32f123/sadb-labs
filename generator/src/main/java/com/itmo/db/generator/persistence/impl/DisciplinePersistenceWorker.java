package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Discipline;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.DisciplinePostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.DisciplinePostgresRepository;

import java.util.List;

public class DisciplinePersistenceWorker extends AbstractPersistenceWorker<Discipline, Integer> {

    private final DisciplinePostgresRepository disciplinePostgresRepository;

    public DisciplinePersistenceWorker(Generator generator, DisciplinePostgresRepository disciplinePostgresRepository) {
        super(Discipline.class, generator);
        this.disciplinePostgresRepository = disciplinePostgresRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Discipline entity) {
        DisciplinePostgresDAO disciplinePostgresDAO = new DisciplinePostgresDAO(
                null,
                entity.getName(),
                entity.getControlForm(),
                entity.getLectureHours(),
                entity.getPracticeHours(),
                entity.getLabHours()
        );

        this.disciplinePostgresRepository.save(disciplinePostgresDAO);
        return List.of(disciplinePostgresDAO);
    }

    @Override
    protected void doCommit() {
        this.disciplinePostgresRepository.flush();
    }
}
