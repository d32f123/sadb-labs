package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.link.PersonGroupLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class PersonGroupLinkPersistenceWorker extends AbstractPersistenceWorker<PersonGroupLink, PersonGroupLink.PersonGroupLinkPK> {


    public PersonGroupLinkPersistenceWorker(Generator generator) {
        super(PersonGroupLink.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(PersonGroupLink entity) {


//        this.personGroupLinkOracleRepository.save(personGroupLinkOracleDAO);
        return null;
    }

    @Override
    protected void doCommit() {
//        this.personGroupLinkOracleRepository.flush();
    }
}
