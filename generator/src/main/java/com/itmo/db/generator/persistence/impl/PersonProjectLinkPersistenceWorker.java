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

    private final PersonProjectLinkRepository personProjectRepository;

    public PersonProjectLinkPersistenceWorker(Generator generator, PersonProjectLinkRepository personProjectRepository) {
        super(PersonProjectLink.class, generator);
        this.personProjectRepository = personProjectRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(PersonProjectLink entity) {
        PersonDAO personDAO = new PersonDAO(this.getDependencyDAOId(Person.class, entity.getId().getPersonId(), PersonDAO.class), null, null, null, null);
        ProjectDAO projectDAO = new ProjectDAO(this.getDependencyDAOId(Project.class, entity.getId().getProjectId(), ProjectDAO.class), null);

        PersonProjectLinkDAO personProjectLinkDAO = new PersonProjectLinkDAO(
                personDAO,
                projectDAO,
                entity.getParticipationStart(),
                entity.getParticipationEnd()
        );

        this.personProjectRepository.save(personProjectLinkDAO);
        return Collections.singletonList(personProjectLinkDAO);
    }

    @Override
    protected void doCommit() {
        this.personProjectRepository.flush();
    }
}
