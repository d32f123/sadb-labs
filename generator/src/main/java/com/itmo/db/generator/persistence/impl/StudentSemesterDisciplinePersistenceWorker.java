package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.*;
import com.itmo.db.generator.persistence.db.postgres.repository.StudentSemesterDisciplinePostgresRepository;
import com.itmo.db.generator.persistence.impl.itmo.ItmoEntityAbstractPersistenceWorker;

import java.util.List;

public class StudentSemesterDisciplinePersistenceWorker
        extends AbstractPersistenceWorker<StudentSemesterDiscipline, StudentSemesterDiscipline.StudentSemesterDisciplinePK> {

    private final StudentSemesterDisciplinePostgresRepository studentSemesterDisciplinePostgresRepository;
    private final ItmoEntityAbstractPersistenceWorker<
            StudentSemesterDiscipline,
            StudentSemesterDiscipline.StudentSemesterDisciplinePK
            > worker;


    public StudentSemesterDisciplinePersistenceWorker(
            Generator generator,
            StudentSemesterDisciplinePostgresRepository studentSemesterDisciplinePostgresRepository,
            ItmoEntityAbstractPersistenceWorker<StudentSemesterDiscipline, StudentSemesterDiscipline.StudentSemesterDisciplinePK> worker) {
        super(StudentSemesterDiscipline.class, generator);
        this.studentSemesterDisciplinePostgresRepository = studentSemesterDisciplinePostgresRepository;
        this.worker = worker;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(StudentSemesterDiscipline entity) {
        StudentSemesterDisciplinePostgresDAO postgresDAO = new StudentSemesterDisciplinePostgresDAO(
                this.getDependencyDAOId(Student.class, entity.getId().getStudentId(), StudentPostgresDAO.class),
                this.getDependencyDAOId(Semester.class, entity.getId().getSemesterId(), SemesterPostgresDAO.class),
                this.getDependencyDAOId(Discipline.class, entity.getId().getDisciplineId(), DisciplinePostgresDAO.class),
                this.getDependencyDAOId(Professor.class, entity.getProfessorId(), ProfessorPostgresDAO.class),
                entity.getSemesterCounter(),
                entity.getScore(),
                entity.getScoreDate()
        );

        var oracleDAO = this.worker.persist(entity);
        return List.of(postgresDAO, oracleDAO);
    }

    @Override
    protected void doCommit() {
        this.studentSemesterDisciplinePostgresRepository.flush();
        this.worker.commit();
    }
}
