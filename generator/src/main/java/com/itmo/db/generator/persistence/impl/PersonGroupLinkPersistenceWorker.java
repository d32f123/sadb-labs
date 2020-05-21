package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Group;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.link.PersonGroupLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.oracle.dao.GroupOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.dao.PersonGroupLinkOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.dao.PersonOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.repository.GroupOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.PersonGroupLinkOracleRepository;

import java.util.List;

public class PersonGroupLinkPersistenceWorker extends AbstractPersistenceWorker<PersonGroupLink, PersonGroupLink.PersonGroupLinkPK> {

    private final PersonGroupLinkOracleRepository personGroupLinkOracleRepository;

    public PersonGroupLinkPersistenceWorker(Generator generator, PersonGroupLinkOracleRepository personGroupLinkOracleRepository) {
        super(PersonGroupLink.class, generator);
        this.personGroupLinkOracleRepository = personGroupLinkOracleRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(PersonGroupLink entity) {
        PersonOracleDAO personOracleDAO = new PersonOracleDAO(this.getDependencyDAOId(Person.class, entity.getId().getPersonId(), PersonOracleDAO.class), null, null, null, null, null);
        GroupOracleDAO groupOracleDAO = new GroupOracleDAO(this.getDependencyDAOId(Group.class, entity.getId().getGroupId(), GroupOracleDAO.class), null, null, null, null);

        PersonGroupLinkOracleDAO personGroupLinkOracleDAO = new PersonGroupLinkOracleDAO(
                personOracleDAO,
                groupOracleDAO
        );

        this.personGroupLinkOracleRepository.save(personGroupLinkOracleDAO);
        return List.of(personGroupLinkOracleDAO);
    }

    @Override
    protected void doCommit() {
        this.personGroupLinkOracleRepository.flush();
    }
}
