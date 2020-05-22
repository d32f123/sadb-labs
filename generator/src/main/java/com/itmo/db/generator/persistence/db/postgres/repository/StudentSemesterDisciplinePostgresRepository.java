package com.itmo.db.generator.persistence.db.postgres.repository;

import com.itmo.db.generator.persistence.db.postgres.dao.StudentSemesterDisciplinePostgresDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSemesterDisciplinePostgresRepository
        extends JpaRepository<StudentSemesterDisciplinePostgresDAO,
        StudentSemesterDisciplinePostgresDAO.StudentSemesterDisciplinePostgresPK> {
}
