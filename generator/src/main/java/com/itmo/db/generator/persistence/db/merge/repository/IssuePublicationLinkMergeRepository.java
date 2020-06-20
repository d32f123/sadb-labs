package com.itmo.db.generator.persistence.db.merge.repository;

import com.itmo.db.generator.model.entity.link.IssuePublicationLink;
import org.springframework.data.jpa.repository.JpaRepository;

// yes
// yes
// yes
public interface IssuePublicationLinkMergeRepository
        extends JpaRepository<IssuePublicationLink, IssuePublicationLink.IssuePublicationLinkPK> {
}
