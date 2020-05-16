package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.link.PersonProjectLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.Collections;
import java.util.List;

public class PersonProjectLinkPersistenceWorker extends AbstractPersistenceWorker<PersonProjectLink, PersonProjectLink.PersonProjectLinkPK> {

    public PersonProjectLinkPersistenceWorker(Generator generator) {
        super(PersonProjectLink.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(PersonProjectLink entity) {
        return Collections.emptyList();
    }

    @Override
    protected void doCommit() {

    }
}
