package com.itmo.db.generator.persistence.db.postgres.repository;

import com.itmo.db.generator.persistence.db.merge.annotations.FetchRepository;
import com.itmo.db.generator.persistence.db.postgres.dao.DisciplinePostgresDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@FetchRepository
@Repository("DisciplinePostgresRepository")
public interface DisciplinePostgresRepository extends JpaRepository<DisciplinePostgresDAO, Integer> {
}
