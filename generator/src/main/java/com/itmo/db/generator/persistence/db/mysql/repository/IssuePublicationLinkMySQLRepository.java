package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.merge.annotations.FetchRepository;
import com.itmo.db.generator.persistence.db.mysql.dao.IssuePublicationLinkMySQLDAO;
import org.springframework.data.jpa.repository.JpaRepository;

@FetchRepository
public interface IssuePublicationLinkMySQLRepository
        extends JpaRepository<IssuePublicationLinkMySQLDAO, IssuePublicationLinkMySQLDAO.IssuePublicationLinkPK> {
}
