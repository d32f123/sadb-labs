package com.itmo.db.generator.generator.entity;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.impl.FacultyGenerator;
import com.itmo.db.generator.generator.entity.impl.PersonGenerator;
import com.itmo.db.generator.generator.entity.impl.ProjectGenerator;
import com.itmo.db.generator.generator.entity.impl.link.PersonProjectLinkGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.model.entity.Faculty;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Project;
import com.itmo.db.generator.model.entity.link.PersonProjectLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EntityGeneratorFactory {

    @Autowired
    private Generator generator;

    public EntityGeneratorFactory() {}

    public <T extends AbstractEntity<TId>, TId> EntityGenerator getGenerator(Class<T> entityClass,
                                                                      Set<DependencyDefinition<?, ?>> deps) {
        if (entityClass.equals(Person.class)) {
            return new PersonGenerator(deps, generator);
        } else if (entityClass.equals(Project.class)) {
            return new ProjectGenerator(deps, generator);
        } else if (entityClass.equals(PersonProjectLink.class)) {
            return new PersonProjectLinkGenerator(deps, generator);
        } else if (entityClass.equals(Faculty.class)) {
            return new FacultyGenerator(deps, generator);
        }

        throw new NullPointerException();
    }

}
