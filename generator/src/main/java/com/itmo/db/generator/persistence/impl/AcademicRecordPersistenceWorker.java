package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.AcademicRecord;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.impl.itmo.ItmoEntityAbstractPersistenceWorker;

import java.util.List;

public class AcademicRecordPersistenceWorker extends AbstractPersistenceWorker<AcademicRecord, Integer> {

    private final ItmoEntityAbstractPersistenceWorker<AcademicRecord, Integer> academicRecordOracleWorker;

    public AcademicRecordPersistenceWorker(Generator generator,
                                           ItmoEntityAbstractPersistenceWorker<AcademicRecord, Integer> worker) {
        super(AcademicRecord.class, generator);
        this.academicRecordOracleWorker = worker;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(AcademicRecord entity) {
        var academicRecordOracleDAO = academicRecordOracleWorker.persist(entity);
        return List.of(academicRecordOracleDAO);
    }

    @Override
    protected void doCommit() {
        this.academicRecordOracleWorker.commit();
    }
}
