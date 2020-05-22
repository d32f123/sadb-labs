package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.ScheduleRecord;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class ScheduleRecordPersistenceWorker extends AbstractPersistenceWorker<ScheduleRecord, Integer> {

    public ScheduleRecordPersistenceWorker(Generator generator) {
        super(ScheduleRecord.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(ScheduleRecord entity) {
        return null;
    }

    @Override
    protected void doCommit() {
    }
}
