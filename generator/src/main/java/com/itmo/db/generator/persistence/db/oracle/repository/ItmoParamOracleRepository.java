package com.itmo.db.generator.persistence.db.oracle.repository;

import com.itmo.db.generator.persistence.db.oracle.dao.ItmoObjectOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.dao.ItmoParamOracleDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItmoParamOracleRepository extends JpaRepository<ItmoParamOracleDAO, Long> {
    List<ItmoParamOracleDAO> findByObjectId(Long objectId);
}
