package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PersonDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.PersonRepository;

import java.util.Collections;
import java.util.List;

public class PersonPersistenceWorker extends AbstractPersistenceWorker<Person, Integer> {

    private final PersonRepository personRepository;

    public PersonPersistenceWorker(Generator generator, PersonRepository personRepository) {
        super(Person.class, generator);
        this.personRepository = personRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Person entity) {
        PersonDAO personDAO = new PersonDAO(
                entity.getId().longValue(),
                entity.getLastName(),
                entity.getFirstName(),
                entity.getPatronymicName(),
                entity.getRole()
        );

        this.personRepository.save(personDAO);
        return Collections.singletonList(personDAO);
    }

    @Override
    protected void doCommit() {
        this.personRepository.flush();
    }
}
