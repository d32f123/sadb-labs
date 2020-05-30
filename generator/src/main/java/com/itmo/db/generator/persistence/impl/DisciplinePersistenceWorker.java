package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Discipline;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.oracle.dao.ItmoObjectOracleDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.DisciplinePostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.DisciplinePostgresRepository;
import com.itmo.db.generator.persistence.impl.itmo.ItmoEntityAbstractPersistenceWorker;

import java.util.List;

public class DisciplinePersistenceWorker extends AbstractPersistenceWorker<Discipline, Integer> {

    private final DisciplinePostgresRepository disciplinePostgresRepository;
    private final ItmoEntityAbstractPersistenceWorker<Discipline, Integer> disciplineOracleWorker;

    public DisciplinePersistenceWorker(Generator generator,
                                       DisciplinePostgresRepository disciplinePostgresRepository,
                                       ItmoEntityAbstractPersistenceWorker<Discipline, Integer> disciplineOracleWorker) {
        super(Discipline.class, generator);
        this.disciplinePostgresRepository = disciplinePostgresRepository;
        this.disciplineOracleWorker = disciplineOracleWorker;
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
        ItmoObjectOracleDAO disciplineOracleDAO = this.disciplineOracleWorker.persist(entity);
        return List.of(disciplinePostgresDAO, disciplineOracleDAO);
    }

    @Override
    protected void doCommit() {
        this.disciplinePostgresRepository.flush();
        this.disciplineOracleWorker.commit();
    }
}
