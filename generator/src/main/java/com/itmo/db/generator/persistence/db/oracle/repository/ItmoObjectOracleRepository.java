package com.itmo.db.generator.persistence.db.oracle.repository;

import com.itmo.db.generator.persistence.db.oracle.dao.ItmoObjectOracleDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItmoObjectOracleRepository extends JpaRepository<ItmoObjectOracleDAO, Long> {
}
