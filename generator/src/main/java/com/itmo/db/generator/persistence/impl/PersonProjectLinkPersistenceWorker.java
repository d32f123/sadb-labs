package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.link.PersonProjectLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;

public class PersonProjectLinkPersistenceWorker extends AbstractPersistenceWorker<PersonProjectLink> {

    public PersonProjectLinkPersistenceWorker(Generator generator) {
        super(PersonProjectLink.class, generator);
    }

    @Override
    protected void doPersist(PersonProjectLink entity) {

    }

    @Override
    protected void doCommit() {

    }
}
