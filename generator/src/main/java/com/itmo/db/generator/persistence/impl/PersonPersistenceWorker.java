package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.mysql.dao.PersonDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.PersonRepository;

public class PersonPersistenceWorker extends AbstractPersistenceWorker<Person> {

    private final PersonRepository personRepository;

    public PersonPersistenceWorker(Generator generator, PersonRepository personRepository) {
        super(Person.class, generator);
        this.personRepository = personRepository;
    }

    @Override
    protected void doPersist(Person entity) {
        PersonDAO personDAO = new PersonDAO(
                entity.getId(),
                entity.getLastName(),
                entity.getFirstName(),
                entity.getPatronymicName(),
                entity.getRole()
        );

        this.personRepository.save(personDAO);
    }

    @Override
    protected void doCommit() {

    }
}
