package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Project;
import com.itmo.db.generator.model.entity.link.PersonProjectLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.*;
import com.itmo.db.generator.persistence.db.mysql.repository.*;

import java.util.*;

public class PersonProjectLinkPersistenceWorker extends AbstractPersistenceWorker<PersonProjectLink, PersonProjectLink.PersonProjectLinkPK> {

    private final PersonProjectLinkMySQLRepository personProjectRepository;

    public PersonProjectLinkPersistenceWorker(Generator generator, PersonProjectLinkMySQLRepository personProjectRepository) {
        super(PersonProjectLink.class, generator);
        this.personProjectRepository = personProjectRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(PersonProjectLink entity) {
        PersonMySQLDAO personMySQLDAO = new PersonMySQLDAO(this.getDependencyDAOId(Person.class, entity.getId().getPersonId(), PersonMySQLDAO.class), null, null, null, null);
        ProjectMySQLDAO projectMySQLDAO = new ProjectMySQLDAO(this.getDependencyDAOId(Project.class, entity.getId().getProjectId(), ProjectMySQLDAO.class), null);

        PersonProjectLinkMySQLDAO personProjectLinkMySQLDAO = new PersonProjectLinkMySQLDAO(
                personMySQLDAO,
                projectMySQLDAO,
                entity.getStartDate(),
                entity.getEndDate()
        );

        this.personProjectRepository.save(personProjectLinkMySQLDAO);
        return Collections.singletonList(personProjectLinkMySQLDAO);
    }

    @Override
    protected void doCommit() {
        this.personProjectRepository.flush();
    }
}
