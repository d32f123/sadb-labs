package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.AcademicRecord;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.oracle.dao.AcademicRecordOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.repository.AcademicRecordOracleRepository;

import java.util.List;

public class AcademicRecordPersistenceWorker extends AbstractPersistenceWorker<AcademicRecord, Integer> {

    private final AcademicRecordOracleRepository academicRecordOracleRepository;

    public AcademicRecordPersistenceWorker(Generator generator, AcademicRecordOracleRepository academicRecordOracleRepository) {
        super(AcademicRecord.class, generator);
        this.academicRecordOracleRepository = academicRecordOracleRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(AcademicRecord entity) {
        AcademicRecordOracleDAO academicRecordOracleDAO = new AcademicRecordOracleDAO(
                null,
                null,
                entity.getAcademicYear(),
                entity.getDegree(),
                entity.isBudget(),
                entity.isFullTime(),
                entity.getDirection(),
                entity.getSpecialty(),
                entity.getPosition(),
                entity.getSubdivision(),
                entity.getStartDate(),
                entity.getEndDate()
        );

        this.academicRecordOracleRepository.save(academicRecordOracleDAO);
        return List.of(academicRecordOracleDAO);
    }

    @Override
    protected void doCommit() {
        this.academicRecordOracleRepository.flush();
    }
}
