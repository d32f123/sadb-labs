package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.mysql.dao.IssueDAO;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IssueRepository extends JpaRepository<IssueDAO, Long> {
}
