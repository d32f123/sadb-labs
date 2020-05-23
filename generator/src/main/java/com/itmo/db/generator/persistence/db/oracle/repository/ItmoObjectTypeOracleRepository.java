package com.itmo.db.generator.persistence.db.oracle.repository;

import com.itmo.db.generator.persistence.db.oracle.dao.ItmoObjectTypeOracleDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItmoObjectTypeOracleRepository extends JpaRepository<ItmoObjectTypeOracleDAO, Long> {
}
