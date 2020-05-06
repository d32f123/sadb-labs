package com.itmo.db.generator.generator.entity;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.impl.PersonGenerator;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Project;

public class EntityGeneratorFactory {

    private static EntityGeneratorFactory instance;

    private EntityGeneratorFactory() {}

    public synchronized static EntityGeneratorFactory getInstance() {
        if (EntityGeneratorFactory.instance == null) {
            EntityGeneratorFactory.instance = new EntityGeneratorFactory();
        }

        return instance;
    }

    public <T extends AbstractEntity> EntityGenerator getGenerator(Class<T> entityClass, Generator generator) {
        if (entityClass.equals(Person.class)) {
            return new PersonGenerator(generator);
        } else if (entityClass.equals(Project.class)) {
            throw new NullPointerException();
        }

        throw new NullPointerException();
    }

}
