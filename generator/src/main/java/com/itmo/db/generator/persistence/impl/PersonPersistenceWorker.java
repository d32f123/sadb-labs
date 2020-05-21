package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PersonMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.PersonMySQLRepository;

import java.util.Collections;
import java.util.List;

public class PersonPersistenceWorker extends AbstractPersistenceWorker<Person, Integer> {

    private final PersonMySQLRepository personMySQLRepository;

    public PersonPersistenceWorker(Generator generator, PersonMySQLRepository personMySQLRepository) {
        super(Person.class, generator);
        this.personMySQLRepository = personMySQLRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Person entity) {
        PersonMySQLDAO personMySQLDAO = new PersonMySQLDAO(
                null,
                entity.getLastName(),
                entity.getFirstName(),
                entity.getPatronymicName(),
                entity.getRole()
        );

        this.personMySQLRepository.save(personMySQLDAO);
        return Collections.singletonList(personMySQLDAO);
    }

    @Override
    protected void doCommit() {
        this.personMySQLRepository.flush();
    }
}
