package com.itmo.db.generator.persistence.db.postgres.repository;

import com.itmo.db.generator.persistence.db.postgres.dao.PersonPostgresDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonPostgresRepository extends JpaRepository<PersonPostgresDAO, Integer> {
}
