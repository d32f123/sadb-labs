package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.model.entity.link.PersonPublicationLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PersonMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PersonPublicationLinkMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PublicationMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.PersonPublicationLinkMySQLRepository;

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
        PersonPublicationLinkMySQLDAO personPublicationLinkMySQLDAO = new PersonPublicationLinkMySQLDAO(
                this.getDependencyDAOId(Person.class, entity.getId().getPersonId(), PersonMySQLDAO.class),
                this.getDependencyDAOId(Publication.class, entity.getId().getPublicationId(), PublicationMySQLDAO.class)
        );

        this.repository.save(personPublicationLinkMySQLDAO);
        return List.of(personPublicationLinkMySQLDAO);
    }

    @Override
    protected void doCommit() {
        this.repository.flush();
    }
}

