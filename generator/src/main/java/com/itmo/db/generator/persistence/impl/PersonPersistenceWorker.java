package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;

public class PersonPersistenceWorker extends AbstractPersistenceWorker<Person> {

    public PersonPersistenceWorker(Generator generator) {
        super(Person.class, generator);
    }

    @Override
    protected void doPersist(Person entity) {

    }

    @Override
    protected void doCommit() {

    }
}
