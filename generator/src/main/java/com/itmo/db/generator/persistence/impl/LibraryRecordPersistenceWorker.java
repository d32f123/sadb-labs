package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.LibraryRecord;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.LibraryRecordMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.LibraryRecordMySQLRepository;

import java.util.List;

public class LibraryRecordPersistenceWorker extends AbstractPersistenceWorker<LibraryRecord, Integer> {

    private final LibraryRecordMySQLRepository libraryRecordMySQLRepository;

    public LibraryRecordPersistenceWorker(Generator generator,
                                          LibraryRecordMySQLRepository libraryRecordMySQLRepository) {
        super(LibraryRecord.class, generator);
        this.libraryRecordMySQLRepository = libraryRecordMySQLRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(LibraryRecord entity) {
        var dao = new LibraryRecordMySQLDAO();

        this.libraryRecordMySQLRepository.save(dao);
        return List.of(dao);
    }

    @Override
    protected void doCommit() {
        this.libraryRecordMySQLRepository.flush();
    }
}