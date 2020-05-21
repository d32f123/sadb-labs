package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Group;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.oracle.dao.GroupOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.repository.GroupOracleRepository;

import java.util.List;

public class GroupPersistenceWorker extends AbstractPersistenceWorker<Group, Integer> {

    private final GroupOracleRepository groupOracleRepository;

    public GroupPersistenceWorker(Generator generator, GroupOracleRepository groupOracleRepository) {
        super(Group.class, generator);
        this.groupOracleRepository = groupOracleRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Group entity) {

        GroupOracleDAO groupOracleDAO = new GroupOracleDAO(
                null,
                entity.getName(),
                entity.getCourse(),
                entity.getStartDate(),
                entity.getEndDate()
        );

        this.groupOracleRepository.save(groupOracleDAO);
        return List.of(groupOracleDAO);
    }

    @Override
    protected void doCommit() {
        this.groupOracleRepository.flush();
    }
}
