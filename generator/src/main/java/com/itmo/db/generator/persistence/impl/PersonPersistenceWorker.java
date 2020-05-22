package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.oracle.dao.PersonOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.repository.PersonOracleRepository;
import com.itmo.db.generator.persistence.db.mysql.dao.PersonMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.PersonMySQLRepository;

import java.util.List;

public class PersonPersistenceWorker extends AbstractPersistenceWorker<Person, Integer> {

    private final PersonOracleRepository personOracleRepository;
    private final PersonMySQLRepository personMySQLRepository;

    public PersonPersistenceWorker(Generator generator, PersonMySQLRepository personMySQLRepository, PersonOracleRepository personOracleRepository) {
        super(Person.class, generator);
        this.personOracleRepository = personOracleRepository;
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
        PersonOracleDAO personOracleDAO = new PersonOracleDAO(
                null,
                entity.getLastName(),
                entity.getFirstName(),
                entity.getPatronymicName(),
                entity.getBirthDate(),
                entity.getBirthPlace()
        );

        this.personMySQLRepository.save(personMySQLDAO);
        this.personOracleRepository.save(personOracleDAO);
        return List.of(personMySQLDAO, personOracleDAO);
    }

    @Override
    protected void doCommit() {
        this.personOracleRepository.flush();
        this.personMySQLRepository.flush();
    }
}
