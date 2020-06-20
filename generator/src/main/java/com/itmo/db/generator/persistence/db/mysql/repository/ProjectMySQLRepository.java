package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.merge.annotations.FetchRepository;
import com.itmo.db.generator.persistence.db.mysql.dao.ProjectMySQLDAO;
import org.springframework.data.jpa.repository.JpaRepository;

@FetchRepository
public interface ProjectMySQLRepository extends JpaRepository<ProjectMySQLDAO, Long> {
}
