package com.itmo.db.generator.persistence.db.postgres.repository;

import com.itmo.db.generator.persistence.db.merge.annotations.FetchRepository;
import com.itmo.db.generator.persistence.db.postgres.dao.SpecialtyPostgresDAO;
import org.springframework.data.jpa.repository.JpaRepository;

@FetchRepository
public interface SpecialtyPostgresRepository extends JpaRepository<SpecialtyPostgresDAO, Integer> {
}
