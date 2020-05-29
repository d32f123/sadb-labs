package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Issue;
import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.model.entity.link.IssuePublicationLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.IssueMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.IssuePublicationLinkMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PublicationMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.IssuePublicationLinkMySQLRepository;

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
        IssuePublicationLinkMySQLDAO issuePublicationLinkMySQLDAO = new IssuePublicationLinkMySQLDAO(
                this.getDependencyDAOId(Issue.class, entity.getId().getIssueId(), IssueMySQLDAO.class),
                this.getDependencyDAOId(Publication.class, entity.getId().getPublicationId(), PublicationMySQLDAO.class)
        );

        this.repository.save(issuePublicationLinkMySQLDAO);
        return List.of(issuePublicationLinkMySQLDAO);
    }

    @Override
    protected void doCommit() {
        this.repository.flush();
    }
}
