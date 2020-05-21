package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Conference;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.ConferenceDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.ConferenceRepository;

import java.util.Collections;
import java.util.List;

public class ConferencePersistenceWorker extends AbstractPersistenceWorker<Conference, Integer> {

    private final ConferenceRepository conferenceRepository;

    public ConferencePersistenceWorker(Generator generator, ConferenceRepository projectRepository) {
        super(Conference.class, generator);
        this.conferenceRepository = projectRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Conference entity) {
        ConferenceDAO conferenceDAO = new ConferenceDAO(
                null,
                entity.getName(),
                entity.getLocation(),
                entity.getDate()
        );

        this.conferenceRepository.save(conferenceDAO);
        return Collections.singletonList(conferenceDAO);
    }

    @Override
    protected void doCommit() {
        this.conferenceRepository.flush();
    }
}
