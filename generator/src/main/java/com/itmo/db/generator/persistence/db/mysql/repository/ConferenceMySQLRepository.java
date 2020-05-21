package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.mysql.dao.ConferenceMySQLDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceMySQLRepository extends JpaRepository<ConferenceMySQLDAO, Long> {
}
