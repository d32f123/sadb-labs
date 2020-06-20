package com.itmo.db.generator.persistence.db.oracle.repository;

import com.itmo.db.generator.model.entity.NumericallyIdentifiableEntity;
import com.itmo.db.generator.persistence.db.oracle.dao.ItmoObjectOracleDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItmoObjectOracleRepository extends JpaRepository<ItmoObjectOracleDAO, Long> {

    List<ItmoObjectOracleDAO> findByObjectTypeId(Long objectTypeId);
}
