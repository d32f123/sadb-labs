package com.itmo.db.generator.persistence.db.postgres.repository;

import com.itmo.db.generator.persistence.db.postgres.dao.SpecialtyDisciplineLinkPostgresDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialtyDisciplineLinkPostgresRepository
        extends JpaRepository<SpecialtyDisciplineLinkPostgresDAO, SpecialtyDisciplineLinkPostgresDAO.SpecialtyDisciplineLinkPK> {
}
