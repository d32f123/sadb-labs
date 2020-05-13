package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.pool.EntityPool;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class PersonGenerator extends AbstractEntityGenerator<Person> {

    public PersonGenerator(Set<DependencyDefinition> deps, Generator generator) {
        super(Person.class, deps, generator);
    }

    @Override
    protected Person getEntity() {
        log.debug("Creating Person");
        Person person = new Person(null, "asdf", "qeer", "asdddd", "lol?");
        // Data filling logic goes here
        return person;
    }
}
