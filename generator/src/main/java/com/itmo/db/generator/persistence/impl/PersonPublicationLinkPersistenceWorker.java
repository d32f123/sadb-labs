package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.*;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.*;
import com.itmo.db.generator.persistence.db.mysql.repository.*;

import java.util.List;

public class PersonPublicationLinkPersistenceWorker
        extends AbstractPersistenceWorker<PersonPublicationLink, PersonPublicationLink.PersonPublicationLinkPK> {

    private final PersonPublicationLinkMySQLRepository repository;

    public PersonPublicationLinkPersistenceWorker(Generator generator, PersonPublicationLinkMySQLRepository repository) {
        super(PersonPublicationLink.class, generator);
        this.repository = repository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(PersonPublicationLink entity) {
        PersonMySQLDAO personMySQLDAO = new PersonMySQLDAO(
                this.getDependencyDAOId(Person.class, entity.getId().getPersonId(), PersonMySQLDAO.class),
                null, null, null, null
        );
        PublicationMySQLDAO publicationMySQLDAO = new PublicationMySQLDAO(
                this.getDependencyDAOId(Publication.class, entity.getId().getPublicationId(), PublicationMySQLDAO.class),
                null, null, null, null
        );

        PersonPublicationLinkMySQLDAO personPublicationLinkMySQLDAO = new PersonPublicationLinkMySQLDAO(
                personMySQLDAO, publicationMySQLDAO
        );

        this.repository.save(personPublicationLinkMySQLDAO);
        return List.of(personPublicationLinkMySQLDAO);
    }

    @Override
    protected void doCommit() {
        this.repository.flush();
    }
}

