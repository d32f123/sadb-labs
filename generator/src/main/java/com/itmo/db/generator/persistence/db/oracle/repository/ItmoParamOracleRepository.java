package com.itmo.db.generator.persistence.db.oracle.repository;

import com.itmo.db.generator.persistence.db.oracle.dao.ItmoParamOracleDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItmoParamOracleRepository extends JpaRepository<ItmoParamOracleDAO, Long> {
}
