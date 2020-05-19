package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PublicationDAO;
import com.itmo.db.generator.persistence.db.mysql.repository.PublicationRepository;

import java.util.Collections;
import java.util.List;

public class PublicationPersistenceWorker extends AbstractPersistenceWorker<Publication, Integer> {

    private final PublicationRepository publicationRepository;

    public PublicationPersistenceWorker(Generator generator, PublicationRepository projectRepository) {
        super(Publication.class, generator);
        this.publicationRepository = projectRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(Publication entity) {
        PublicationDAO publicationDAO = new PublicationDAO(
                entity.getId().longValue(),
                entity.getName(),
                entity.getLanguage(),
                entity.getCitation_index(),
                entity.getDate()
        );

        this.publicationRepository.save(publicationDAO);
        return Collections.singletonList(publicationDAO);
    }

    @Override
    protected void doCommit() {
        this.publicationRepository.flush();
    }
}
