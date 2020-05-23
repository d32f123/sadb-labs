package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.AcademicRecord;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class AcademicRecordPersistenceWorker extends AbstractPersistenceWorker<AcademicRecord, Integer> {


    public AcademicRecordPersistenceWorker(Generator generator) {
        super(AcademicRecord.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(AcademicRecord entity) {


//        this.academicRecordOracleRepository.save(academicRecordOracleDAO);
        return null;
    }

    @Override
    protected void doCommit() {
//        this.academicRecordOracleRepository.flush();
    }
}
