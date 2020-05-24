package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.IssuePublicationLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.*;
import com.itmo.db.generator.persistence.db.mysql.repository.*;

import java.util.List;

public class IssuePublicationLinkPersistenceWorker
        extends AbstractPersistenceWorker<IssuePublicationLink, IssuePublicationLink.IssuePublicationLinkPK> {

    private final IssuePublicationLinkMySQLRepository repository;

    public IssuePublicationLinkPersistenceWorker(Generator generator, IssuePublicationLinkMySQLRepository repository) {
        super(IssuePublicationLink.class, generator);
        this.repository = repository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(IssuePublicationLink entity) {
        IssueMySQLDAO issueMySQLDAO = new IssueMySQLDAO(
                this.getDependencyDAOId(Issue.class, entity.getId().getIssueId(), IssueMySQLDAO.class),
                null, null, null, null, null
        );
        PublicationMySQLDAO publicationMySQLDAO = new PublicationMySQLDAO(
                this.getDependencyDAOId(Publication.class, entity.getId().getPublicationId(), PublicationMySQLDAO.class),
                null, null, null, null
        );

        IssuePublicationLinkMySQLDAO issuePublicationLinkMySQLDAO = new IssuePublicationLinkMySQLDAO(
                issueMySQLDAO, publicationMySQLDAO
        );

        this.repository.save(issuePublicationLinkMySQLDAO);
        return List.of(issuePublicationLinkMySQLDAO);
    }

    @Override
    protected void doCommit() {
        this.repository.flush();
    }
}
