package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Issue;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.IssueMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.IssueMySQLRepository;

import java.util.List;

public class IssuePersistenceWorker extends AbstractPersistenceWorker<Issue, Integer> {

    private final IssueMySQLRepository issueMySQLRepository;

    public IssuePersistenceWorker(Generator generator, IssueMySQLRepository issueMySQLRepository) {
        super(Issue.class, generator);
        this.issueMySQLRepository = issueMySQLRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Issue entity) {
        IssueMySQLDAO issueMySQLDAO = new IssueMySQLDAO(
                null,
                entity.getName(),
                entity.getLanguage(),
                entity.getLocation(),
                entity.getLength(),
                entity.getFormat()
        );

        this.issueMySQLRepository.save(issueMySQLDAO);
        return List.of(issueMySQLDAO);
    }

    @Override
    protected void doCommit() {
        this.issueMySQLRepository.flush();
    }
}
