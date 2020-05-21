package com.itmo.db.generator.persistence.db.oracle.repository;

import com.itmo.db.generator.persistence.db.oracle.dao.PersonOracleDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonOracleRepository extends JpaRepository<PersonOracleDAO, Long> {
}
