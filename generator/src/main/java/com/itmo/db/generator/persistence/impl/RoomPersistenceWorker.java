package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Dormitory;
import com.itmo.db.generator.model.entity.Room;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mongo.dao.DormitoryMongoDAO;
import com.itmo.db.generator.persistence.db.mongo.dao.RoomMongoDAO;
import com.itmo.db.generator.persistence.db.mongo.repository.RoomMongoRepository;

import java.sql.Date;
import java.util.List;

public class RoomPersistenceWorker extends AbstractPersistenceWorker<Room, Integer> {

    private final RoomMongoRepository roomMongoRepository;

    public RoomPersistenceWorker(Generator generator,
                                 RoomMongoRepository roomMongoRepository) {
        super(Room.class, generator);
        this.roomMongoRepository = roomMongoRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Room entity) {
        var dao = new RoomMongoDAO(
                null,
                entity.getRoomNumber(),
                (int) entity.getCapacity(),
                (int) entity.getEngaged(),
                entity.getBugs(),
                entity.getLastCleaningDate() != null
                        ? Date.valueOf(entity.getLastCleaningDate())
                        : null,
                this.getDependencyDAOId(Dormitory.class, entity.getDormitoryId(), DormitoryMongoDAO.class)
        );
        this.roomMongoRepository.save(dao);
        return List.of(dao);
    }

    @Override
    protected void doCommit() {
    }
}
