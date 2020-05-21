package com.itmo.db.generator.persistence.db.oracle.repository;

import com.itmo.db.generator.persistence.db.oracle.dao.GroupOracleDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupOracleRepository extends JpaRepository<GroupOracleDAO, Long> {
}
