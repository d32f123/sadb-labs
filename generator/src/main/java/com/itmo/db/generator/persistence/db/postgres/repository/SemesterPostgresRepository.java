package com.itmo.db.generator.persistence.db.postgres.repository;

import com.itmo.db.generator.persistence.db.postgres.dao.SemesterPostgresDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterPostgresRepository extends JpaRepository<SemesterPostgresDAO, Integer> {
}
