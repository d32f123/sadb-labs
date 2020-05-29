package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.link.PersonGroupLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.impl.itmo.ItmoEntityAbstractPersistenceWorker;

import java.util.List;

public class PersonGroupLinkPersistenceWorker extends AbstractPersistenceWorker<PersonGroupLink, PersonGroupLink.PersonGroupLinkPK> {

    private final ItmoEntityAbstractPersistenceWorker<PersonGroupLink, PersonGroupLink.PersonGroupLinkPK> worker;

    public PersonGroupLinkPersistenceWorker(Generator generator,
                                            ItmoEntityAbstractPersistenceWorker<PersonGroupLink, PersonGroupLink.PersonGroupLinkPK> worker) {
        super(PersonGroupLink.class, generator);
        this.worker = worker;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(PersonGroupLink entity) {
        var dao = this.worker.persist(entity);
        return List.of(dao);
    }

    @Override
    protected void doCommit() {
        this.worker.commit();
    }
}
