package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.AccommodationRecord;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class AccommodationRecordPersistenceWorker extends AbstractPersistenceWorker<AccommodationRecord, Integer> {

    public AccommodationRecordPersistenceWorker(Generator generator) {
        super(AccommodationRecord.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(AccommodationRecord entity) {
        return null;
    }

    @Override
    protected void doCommit() {
    }
}
