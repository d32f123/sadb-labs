package com.itmo.db.generator.persistence.db.merge.repository;

import com.itmo.db.generator.model.entity.link.IssuePublicationLink;
import com.itmo.db.generator.persistence.db.merge.annotations.MergeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

// yes
// yes
// yes
@MergeRepository
public interface IssuePublicationLinkMergeRepository
        extends JpaRepository<IssuePublicationLink, IssuePublicationLink.IssuePublicationLinkPK> {
}
