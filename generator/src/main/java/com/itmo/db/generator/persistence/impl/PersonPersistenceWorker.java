package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mongo.dao.PersonMongoDAO;
import com.itmo.db.generator.persistence.db.mongo.repository.PersonMongoRepository;
import com.itmo.db.generator.persistence.db.mysql.dao.PersonMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.PersonMySQLRepository;
import com.itmo.db.generator.persistence.db.oracle.dao.ItmoObjectOracleDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.PersonPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.PersonPostgresRepository;
import com.itmo.db.generator.persistence.impl.itmo.ItmoEntityAbstractPersistenceWorker;

import java.util.List;

public class PersonPersistenceWorker extends AbstractPersistenceWorker<Person, Integer> {

    private final PersonMySQLRepository personMySQLRepository;
    private final PersonPostgresRepository personPostgresRepository;
    private final ItmoEntityAbstractPersistenceWorker<Person, Integer> itmoEntityAbstractPersistenceWorker;
    private final PersonMongoRepository personMongoRepository;

    public PersonPersistenceWorker(
            Generator generator,
            PersonMySQLRepository personMySQLRepository,
            PersonPostgresRepository personPostgresRepository,
            ItmoEntityAbstractPersistenceWorker<Person, Integer> itmoEntityAbstractPersistenceWorker,
            PersonMongoRepository personMongoRepository
    ) {
        super(Person.class, generator);
        this.personMySQLRepository = personMySQLRepository;
        this.personPostgresRepository = personPostgresRepository;
        this.itmoEntityAbstractPersistenceWorker = itmoEntityAbstractPersistenceWorker;
        this.personMongoRepository = personMongoRepository;
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
        PersonMongoDAO personMongoDAO = new PersonMongoDAO(
                null,
                entity.getLastName(),
                entity.getFirstName(),
                entity.getPatronymicName(),
                entity.isInDormitory(),
                (int) entity.getWarningCount()
        );


        this.personMySQLRepository.save(personMySQLDAO);
        this.personPostgresRepository.save(personPostgresDAO);
        ItmoObjectOracleDAO personOracleDAO = this.itmoEntityAbstractPersistenceWorker.persist(entity);
        this.personMongoRepository.save(personMongoDAO);

        return List.of(personMySQLDAO, personPostgresDAO, personOracleDAO, personMongoDAO);
    }

    @Override
    protected void doCommit() {
        this.personMySQLRepository.flush();
        this.personPostgresRepository.flush();
        this.itmoEntityAbstractPersistenceWorker.commit();
    }
}
