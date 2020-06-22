package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Specialty;
import com.itmo.db.generator.model.entity.Student;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class StudentGenerator extends AbstractEntityGenerator<Student, Integer> {

    public StudentGenerator(EntityDefinition<Student, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    Random random = new Random();

    public String getStudyType(Random random) {
        String[] types = new String[]{"очная", "заочная"};
        return types[random.nextInt(types.length)];
    }

    private Student getEntity(Person person, Specialty specialty) {
        return new Student(person.getId(), specialty.getId(), getStudyType(random), person.getPersonNumber(), person);
    }

    @Override
    protected List<Student> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Creating Student");

        return this.getDependencyInstances(Person.class).stream().filter(person -> !person.getRole().equals("docent")).stream().map(
                person -> this.getDependencyInstances(Specialty.class).stream().map(
                        specialty -> getEntity(person, specialty)
                )
        ).reduce(Stream::concat).orElseThrow().collect(Collectors.toList());
    }
}

