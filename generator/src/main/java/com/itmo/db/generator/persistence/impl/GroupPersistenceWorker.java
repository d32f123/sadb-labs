package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Group;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.oracle.dao.ItmoObjectOracleDAO;
import com.itmo.db.generator.persistence.impl.itmo.ItmoGroupPersistenceWorker;

import java.util.List;

public class GroupPersistenceWorker extends AbstractPersistenceWorker<Group, Integer> {

    private final ItmoGroupPersistenceWorker itmoGroupPersistenceWorker;

    public GroupPersistenceWorker(Generator generator, ItmoGroupPersistenceWorker itmoGroupPersistenceWorker) {
        super(Group.class, generator);
        this.itmoGroupPersistenceWorker = itmoGroupPersistenceWorker;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Group entity) {
        ItmoObjectOracleDAO savedOracleGroup = this.itmoGroupPersistenceWorker.persist(entity);
        return List.of(savedOracleGroup);
    }

    @Override
    protected void doCommit() {
        this.itmoGroupPersistenceWorker.commit();
    }
}
