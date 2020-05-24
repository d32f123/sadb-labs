package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PublicationMySQLDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.PublicationMySQLRepository;

import java.util.Collections;
import java.util.List;

public class PublicationPersistenceWorker extends AbstractPersistenceWorker<Publication, Integer> {

    private final PublicationMySQLRepository publicationMySQLRepository;

    public PublicationPersistenceWorker(Generator generator, PublicationMySQLRepository projectRepository) {
        super(Publication.class, generator);
        this.publicationMySQLRepository = projectRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Publication entity) {
        PublicationMySQLDAO publicationMySQLDAO = new PublicationMySQLDAO(
                null,
                entity.getName(),
                entity.getLanguage(),
                entity.getCitation_index(),
                entity.getDate()
        );

        this.publicationMySQLRepository.save(publicationMySQLDAO);
        return List.of(publicationMySQLDAO);
    }

    @Override
    protected void doCommit() {
        this.publicationMySQLRepository.flush();
    }
}
