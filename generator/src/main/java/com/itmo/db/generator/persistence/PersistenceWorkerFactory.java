package com.itmo.db.generator.persistence;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Project;
import com.itmo.db.generator.model.entity.link.PersonProjectLink;
import com.itmo.db.generator.persistence.impl.PersonPersistenceWorker;
import com.itmo.db.generator.persistence.impl.PersonProjectLinkPersistenceWorker;
import com.itmo.db.generator.persistence.impl.ProjectPersistenceWorker;

public class PersistenceWorkerFactory {

    private static PersistenceWorkerFactory instance;

    private PersistenceWorkerFactory() {}

    public synchronized static PersistenceWorkerFactory getInstance() {
        if (instance == null) {
            instance = new PersistenceWorkerFactory();
        }

        return instance;
    }

    public <T extends AbstractEntity> PersistenceWorker getWorker(Class<T> entityClass, Generator generator) {
        if (entityClass.equals(Person.class)) {
            return new PersonPersistenceWorker(generator);
        } else if (entityClass.equals(Project.class)) {
            return new ProjectPersistenceWorker(generator);
        } else if (entityClass.equals(PersonProjectLink.class)) {
            return new PersonProjectLinkPersistenceWorker(generator);
        }

        throw new NullPointerException();
    }

}
