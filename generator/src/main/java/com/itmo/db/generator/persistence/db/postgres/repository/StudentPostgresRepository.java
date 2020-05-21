package com.itmo.db.generator.persistence.db.postgres.repository;

import com.itmo.db.generator.persistence.db.postgres.dao.StudentPostgresDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentPostgresRepository extends JpaRepository<StudentPostgresDAO, Integer> {
}
