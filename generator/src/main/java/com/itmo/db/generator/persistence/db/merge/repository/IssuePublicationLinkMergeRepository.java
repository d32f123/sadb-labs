package com.itmo.db.generator.persistence.db.merge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuePublicationLinkMergeRepository
        extends JpaRepository<IssuePublicationLink, IssuePublicationLink.IssuePublicationLinkPK> {
}
