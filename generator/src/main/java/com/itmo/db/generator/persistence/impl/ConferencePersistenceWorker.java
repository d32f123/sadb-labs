package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Conference;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.ConferenceMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.ConferenceMySQLRepository;

import java.util.Collections;
import java.util.List;

public class ConferencePersistenceWorker extends AbstractPersistenceWorker<Conference, Integer> {

    private final ConferenceMySQLRepository conferenceMySQLRepository;

    public ConferencePersistenceWorker(Generator generator, ConferenceMySQLRepository projectRepository) {
        super(Conference.class, generator);
        this.conferenceMySQLRepository = projectRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Conference entity) {
        ConferenceMySQLDAO conferenceMySQLDAO = new ConferenceMySQLDAO(
                null,
                entity.getName(),
                entity.getLocation(),
                entity.getDate()
        );

        this.conferenceMySQLRepository.save(conferenceMySQLDAO);
        return List.of(conferenceMySQLDAO);
    }

    @Override
    protected void doCommit() {
        this.conferenceMySQLRepository.flush();
    }
}
