package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.link.IssuePublicationLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class IssuePublicationLinkPersistenceWorker
        extends AbstractPersistenceWorker<IssuePublicationLink, IssuePublicationLink.IssuePublicationLinkPK> {

    public IssuePublicationLinkPersistenceWorker(Generator generator) {
        super(IssuePublicationLink.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(IssuePublicationLink entity) {
        return null;
    }

    @Override
    protected void doCommit() {
    }
}
