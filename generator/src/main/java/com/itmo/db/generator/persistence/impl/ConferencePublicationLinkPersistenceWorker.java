package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Conference;
import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.model.entity.link.ConferencePublicationLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.ConferenceMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.ConferencePublicationLinkMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PublicationMySQLDAO;
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
        ConferencePublicationLinkMySQLDAO conferencePublicationLinkMySQLDAO = new ConferencePublicationLinkMySQLDAO(
                this.getDependencyDAOId(Conference.class, entity.getId().getConferenceId(), ConferenceMySQLDAO.class),
                this.getDependencyDAOId(Publication.class, entity.getId().getPublicationId(), PublicationMySQLDAO.class)
        );

        this.repository.save(conferencePublicationLinkMySQLDAO);
        return List.of(conferencePublicationLinkMySQLDAO);
    }

    @Override
    protected void doCommit() {
        this.repository.flush();
    }
}

