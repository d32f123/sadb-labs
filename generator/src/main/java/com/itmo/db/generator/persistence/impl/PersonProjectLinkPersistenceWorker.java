package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Project;
import com.itmo.db.generator.model.entity.link.PersonProjectLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PersonMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PersonProjectLinkMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.ProjectMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.PersonProjectLinkMySQLRepository;

import java.util.List;

public class PersonProjectLinkPersistenceWorker extends AbstractPersistenceWorker<PersonProjectLink, PersonProjectLink.PersonProjectLinkPK> {

    private final PersonProjectLinkMySQLRepository personProjectLinkMySQLRepository;

    public PersonProjectLinkPersistenceWorker(Generator generator, PersonProjectLinkMySQLRepository personProjectLinkMySQLRepository) {
        super(PersonProjectLink.class, generator);
        this.personProjectLinkMySQLRepository = personProjectLinkMySQLRepository;
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

        this.personProjectLinkMySQLRepository.save(personProjectLinkMySQLDAO);
        return List.of(personProjectLinkMySQLDAO);
    }

    @Override
    protected void doCommit() {
        this.personProjectLinkMySQLRepository.flush();
    }
}
