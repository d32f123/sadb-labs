package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.mysql.dao.IssueMySQLDAO;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IssueMySQLRepository extends JpaRepository<IssueMySQLDAO, Long> {
}
