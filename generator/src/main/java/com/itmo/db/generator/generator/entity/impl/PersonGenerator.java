package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.pool.EntityPool;

public class PersonGenerator extends AbstractEntityGenerator<Person> {

    public PersonGenerator(Generator generator) {
        super(Person.class, generator);
    }

    @Override
    protected Person getEntity() {
        Person person = new Person();
        // Data filling logic goes here
        return person;
    }
}
