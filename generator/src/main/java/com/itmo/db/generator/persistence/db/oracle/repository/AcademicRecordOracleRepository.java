package com.itmo.db.generator.persistence.db.oracle.repository;

import com.itmo.db.generator.persistence.db.oracle.dao.AcademicRecordOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.dao.GroupOracleDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicRecordOracleRepository extends JpaRepository<AcademicRecordOracleDAO, Long> {
}
