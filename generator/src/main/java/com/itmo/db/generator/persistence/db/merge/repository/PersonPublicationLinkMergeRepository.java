package com.itmo.db.generator.persistence.db.merge.repository;

import com.itmo.db.generator.model.entity.link.PersonPublicationLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonPublicationLinkMergeRepository
        extends JpaRepository<PersonPublicationLink, PersonPublicationLink.PersonPublicationLinkPK> {
}
