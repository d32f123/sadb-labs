package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.*;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.*;
import com.itmo.db.generator.persistence.db.mysql.repository.ConferencePublicationLinkMySQLRepository;

import java.util.List;

public class ConferencePublicationLinkPersistenceWorker
        extends AbstractPersistenceWorker<ConferencePublicationLink, ConferencePublicationLink.ConferencePublicationLinkPK> {

    private final ConferencePublicationLinkMySQLRepository repository;

    public ConferencePublicationLinkPersistenceWorker(Generator generator, ConferencePublicationLinkMySQLRepository repository) {
        super(ConferencePublicationLink.class, generator);
        this.repository = repository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(ConferencePublicationLink entity) {
        ConferenceMySQLDAO conferenceMySQLDAO = new ConferenceMySQLDAO(
                this.getDependencyDAOId(Conference.class, entity.getId().getConferenceId(), ConferenceMySQLDAO.class),
                null, null, null
        );
        PublicationMySQLDAO publicationMySQLDAO = new PublicationMySQLDAO(
                this.getDependencyDAOId(Publication.class, entity.getId().getPublicationId(), PublicationMySQLDAO.class),
                null, null, null, null
        );

        ConferencePublicationLinkMySQLDAO conferencePublicationLinkMySQLDAO = new ConferencePublicationLinkMySQLDAO(
                conferenceMySQLDAO, publicationMySQLDAO
        );

        this.repository.save(conferencePublicationLinkMySQLDAO);
        return List.of(conferencePublicationLinkMySQLDAO);
    }

    @Override
    protected void doCommit() {
        this.repository.flush();
    }
}

