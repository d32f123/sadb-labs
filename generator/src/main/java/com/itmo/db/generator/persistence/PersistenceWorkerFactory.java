package com.itmo.db.generator.persistence;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.model.entity.Faculty;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Project;
import com.itmo.db.generator.model.entity.link.PersonProjectLink;
import com.itmo.db.generator.persistence.db.postgres.repository.FacultyRepository;
import com.itmo.db.generator.persistence.impl.FacultyPersistenceWorker;
import com.itmo.db.generator.persistence.impl.PersonPersistenceWorker;
import com.itmo.db.generator.persistence.impl.PersonProjectLinkPersistenceWorker;
import com.itmo.db.generator.persistence.impl.ProjectPersistenceWorker;
import com.itmo.db.generator.persistence.db.mysql.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersistenceWorkerFactory {

    @Autowired
    private Generator generator;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    public PersistenceWorkerFactory() {}

    public <T extends AbstractEntity> PersistenceWorker getWorker(Class<T> entityClass) {
        if (entityClass.equals(Person.class)) {
            return new PersonPersistenceWorker(generator, personRepository);
        } else if (entityClass.equals(Project.class)) {
            return new ProjectPersistenceWorker(generator);
        } else if (entityClass.equals(PersonProjectLink.class)) {
            return new PersonProjectLinkPersistenceWorker(generator);
        } else if (entityClass.equals(Faculty.class)) {
            return new FacultyPersistenceWorker(generator, facultyRepository);
        }

        throw new NullPointerException();
    }

}
