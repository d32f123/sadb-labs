package com.itmo.db.generator.persistence.db.oracle.repository;

import com.itmo.db.generator.persistence.db.oracle.dao.ItmoAttributeOracleDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItmoAttributeOracleRepository extends JpaRepository<ItmoAttributeOracleDAO, Long> {
}
