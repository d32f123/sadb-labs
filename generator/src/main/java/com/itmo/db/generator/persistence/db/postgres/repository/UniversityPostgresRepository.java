package com.itmo.db.generator.persistence.db.postgres.repository;

import com.itmo.db.generator.persistence.db.postgres.dao.UniversityPostgresDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityPostgresRepository extends JpaRepository<UniversityPostgresDAO, Integer> {
}
