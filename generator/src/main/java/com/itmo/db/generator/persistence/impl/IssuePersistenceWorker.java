package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Issue;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.IssueDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.IssueRepository;

import java.util.Collections;
import java.util.List;

public class IssuePersistenceWorker extends AbstractPersistenceWorker<Issue, Integer> {

    private final IssueRepository issueRepository;

    public IssuePersistenceWorker(Generator generator, IssueRepository issueRepository) {
        super(Issue.class, generator);
        this.issueRepository = issueRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Issue entity) {
        IssueDAO issueDAO = new IssueDAO(
                null,
                entity.getName(),
                entity.getLanguage(),
                entity.getLocation(),
                entity.getLength(),
                entity.getFormat()
        );

        this.issueRepository.save(issueDAO);
        return Collections.singletonList(issueDAO);
    }

    @Override
    protected void doCommit() {
        this.issueRepository.flush();
    }
}
