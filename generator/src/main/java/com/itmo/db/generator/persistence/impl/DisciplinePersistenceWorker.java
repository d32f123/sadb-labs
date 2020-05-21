package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Discipline;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.DisciplineDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.DisciplineRepository;

import java.util.*;

public class DisciplinePersistenceWorker extends AbstractPersistenceWorker<Discipline, Integer> {

    private final DisciplineRepository disciplineRepository;

    public DisciplinePersistenceWorker(Generator generator, DisciplineRepository disciplineRepository) {
        super(Discipline.class, generator);
        this.disciplineRepository = disciplineRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Discipline entity) {
        DisciplineDAO disciplineDAO = new DisciplineDAO(
                null,
                entity.getName(),
                entity.getControlForm(),
                entity.getLectureHours(),
                entity.getPracticeHours(),
                entity.getLabHours()
        );

        this.disciplineRepository.save(disciplineDAO);
        return Collections.singletonList(disciplineDAO);
    }

    @Override
    protected void doCommit() {
        this.disciplineRepository.flush();
    }
}
