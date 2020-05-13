package com.itmo.db.generator.persistence.db.postgres.repository;

import com.itmo.db.generator.persistence.db.postgres.dao.FacultyDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<FacultyDAO, Integer> {
}
