package com.itmo.db.generator.persistence.db.merge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonPublicationLinkMergeRepository
        extends JpaRepository<PersonPublicationLinkMergedDAO, PersonPublicationLinkMergedDAO.PersonPublicationLinkPK> {
}
