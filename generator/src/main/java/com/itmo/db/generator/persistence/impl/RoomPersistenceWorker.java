package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Room;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class RoomPersistenceWorker extends AbstractPersistenceWorker<Room, Integer> {

    public RoomPersistenceWorker(Generator generator) {
        super(Room.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Room entity) {
        return null;
    }

    @Override
    protected void doCommit() {
    }
}
