package com.itmo.db.generator.generator.entity;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.impl.PersonGenerator;
import com.itmo.db.generator.generator.entity.impl.ProjectGenerator;
import com.itmo.db.generator.generator.entity.impl.link.PersonProjectLinkGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Project;
import com.itmo.db.generator.model.entity.link.PersonProjectLink;

import java.util.Set;

public class EntityGeneratorFactory {

    private static EntityGeneratorFactory instance;

    private EntityGeneratorFactory() {}

    public synchronized static EntityGeneratorFactory getInstance() {
        if (EntityGeneratorFactory.instance == null) {
            EntityGeneratorFactory.instance = new EntityGeneratorFactory();
        }

        return instance;
    }

    public <T extends AbstractEntity> EntityGenerator getGenerator(Class<T> entityClass,
                                                                   Set<DependencyDefinition> deps,
                                                                   Generator generator) {
        if (entityClass.equals(Person.class)) {
            return new PersonGenerator(deps, generator);
        } else if (entityClass.equals(Project.class)) {
            return new ProjectGenerator(deps, generator);
        } else if (entityClass.equals(PersonProjectLink.class)) {
            return new PersonProjectLinkGenerator(deps, generator);
        }

        throw new NullPointerException();
    }

}
