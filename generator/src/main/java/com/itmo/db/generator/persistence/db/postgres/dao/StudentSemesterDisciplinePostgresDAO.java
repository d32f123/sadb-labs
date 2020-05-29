package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "student_semester_disciplines")
@IdClass(StudentSemesterDisciplinePostgresDAO.StudentSemesterDisciplinePostgresPK.class)
@NoArgsConstructor
@AllArgsConstructor
public class StudentSemesterDisciplinePostgresDAO implements IdentifiableDAO<StudentSemesterDisciplinePostgresDAO.StudentSemesterDisciplinePostgresPK> {

    @Id
    @Column(name = "student_id")
    private Integer studentId;
    @Id
    @Column(name = "semester_id")
    private Integer semesterId;
    @Id
    @Column(name = "discipline_id")
    private Integer disciplineId;
    @Column(name = "professor_id")
    private Integer professorId;
    @Column(name = "semester_counter")
    private Integer semesterCounter;
    @Column(name = "score")
    private Integer score;
    @Column(name = "score_date")
    private LocalDate scoreDate;

    @Override
    public StudentSemesterDisciplinePostgresPK getId() {
        return new StudentSemesterDisciplinePostgresPK(
                this.studentId,
                this.semesterId,
                this.disciplineId
        );
    }

    @Override
    public void setId(StudentSemesterDisciplinePostgresPK studentSemesterDisciplinePostgresPK) {
        throw new UnsupportedOperationException("SetId not implemented");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentSemesterDisciplinePostgresPK implements Serializable {
        private Integer student;
        private Integer semester;
        private Integer discipline;
    }
}
