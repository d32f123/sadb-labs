package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.ScheduleRecord;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.impl.itmo.ItmoEntityAbstractPersistenceWorker;

import java.util.List;

public class ScheduleRecordPersistenceWorker extends AbstractPersistenceWorker<ScheduleRecord, Integer> {
    private final ItmoEntityAbstractPersistenceWorker<ScheduleRecord, Integer> worker;

    public ScheduleRecordPersistenceWorker(Generator generator,
                                           ItmoEntityAbstractPersistenceWorker<ScheduleRecord, Integer> worker) {
        super(ScheduleRecord.class, generator);
        this.worker = worker;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(ScheduleRecord entity) {
        var dao = this.worker.persist(entity);
        return List.of(dao);
    }

    @Override
    protected void doCommit() {
        worker.commit();
    }
}
