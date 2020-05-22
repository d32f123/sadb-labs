package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.link.ConferencePublicationLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class ConferencePublicationLinkPersistenceWorker
        extends AbstractPersistenceWorker<ConferencePublicationLink, ConferencePublicationLink.ConferencePublicationLinkPK> {

    public ConferencePublicationLinkPersistenceWorker(Generator generator) {
        super(ConferencePublicationLink.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(ConferencePublicationLink entity) {
        return null;
    }

    @Override
    protected void doCommit() {
    }
}

