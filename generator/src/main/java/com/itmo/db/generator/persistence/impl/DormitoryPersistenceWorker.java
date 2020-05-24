package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Dormitory;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mongo.dao.DormitoryMongoDAO;
import com.itmo.db.generator.persistence.db.mongo.repository.DormitoryMongoRepository;

import java.util.List;

public class DormitoryPersistenceWorker extends AbstractPersistenceWorker<Dormitory, Integer> {

    private final DormitoryMongoRepository repository;

    public DormitoryPersistenceWorker(Generator generator, DormitoryMongoRepository repository) {
        super(Dormitory.class, generator);
        this.repository = repository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Dormitory entity) {
        DormitoryMongoDAO dormitoryMongoDAO = new DormitoryMongoDAO(
                null,
                entity.getAddress(),
                entity.getRoomCount()
        );

        this.repository.save(dormitoryMongoDAO);
        return List.of(dormitoryMongoDAO);
    }

    @Override
    protected void doCommit() {
    }
}
