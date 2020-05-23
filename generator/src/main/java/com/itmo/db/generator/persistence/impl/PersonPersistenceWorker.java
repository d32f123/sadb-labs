package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PersonMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.PersonMySQLRepository;
import com.itmo.db.generator.persistence.db.postgres.dao.PersonPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.PersonPostgresRepository;

import java.util.List;

public class PersonPersistenceWorker extends AbstractPersistenceWorker<Person, Integer> {

    private final PersonMySQLRepository personMySQLRepository;
    private final PersonPostgresRepository personPostgresRepository;

    public PersonPersistenceWorker(
            Generator generator, PersonMySQLRepository personMySQLRepository, PersonPostgresRepository personPostgresRepository
    ) {
        super(Person.class, generator);
        this.personMySQLRepository = personMySQLRepository;
        this.personPostgresRepository = personPostgresRepository;
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
        PersonPostgresDAO personPostgresDAO = new PersonPostgresDAO(
                null,
                entity.getLastName(),
                entity.getFirstName(),
                entity.getPatronymicName(),
                entity.getRole()
        );


        this.personMySQLRepository.save(personMySQLDAO);
//        this.personOracleRepository.save(personOracleDAO);
        this.personPostgresRepository.save(personPostgresDAO);
        return List.of(personMySQLDAO, personPostgresDAO);
    }

    @Override
    protected void doCommit() {
//        this.personOracleRepository.flush();
        this.personMySQLRepository.flush();
        this.personPostgresRepository.flush();
    }
}
