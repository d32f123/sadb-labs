package com.itmo.db.generator.persistence.db.merge.repository;

import com.itmo.db.generator.model.entity.link.ConferencePublicationLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferencePublicationLinkMergeRepository
        extends JpaRepository<ConferencePublicationLink, ConferencePublicationLink.ConferencePublicationLinkPK> {
}
