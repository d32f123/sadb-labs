package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.LibraryRecord;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.LibraryRecordMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PersonMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.LibraryRecordMySQLRepository;

import java.sql.Timestamp;
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
        LibraryRecordMySQLDAO dao = new LibraryRecordMySQLDAO(
                null,
                this.getDependencyDAOId(Person.class, entity.getPersonId(), PersonMySQLDAO.class),
                entity.getBookId(),
                entity.getAction(),
                Timestamp.valueOf(entity.getActionDate().atStartOfDay())
        );

        this.libraryRecordMySQLRepository.save(dao);
        return List.of(dao);
    }

    @Override
    protected void doCommit() {
        this.libraryRecordMySQLRepository.flush();
    }
}
