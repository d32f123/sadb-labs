package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "student_semester_disciplines")
@NoArgsConstructor
@AllArgsConstructor
public class StudentSemesterDisciplinePostgresDAO implements IdentifiableDAO<StudentSemesterDisciplinePostgresDAO.StudentSemesterDisciplinePostgresPK> {

    @Id
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "person_id")
    private StudentPostgresDAO student;
    @Id
    @ManyToOne
    @JoinColumn(name = "semester_id", referencedColumnName = "semester_id")
    private SemesterPostgresDAO semester;
    @Id
    @ManyToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "discipline_id")
    private DisciplinePostgresDAO discipline;
    @ManyToOne
    @JoinColumn(name = "professor_id", referencedColumnName = "person_id")
    private ProfessorPostgresDAO professor;
    @Column(name = "semester_counter")
    private Integer semesterCounter;
    @Column(name = "score")
    private Integer score;
    @Column(name = "score_date")
    private Date scoreDate;

    @Override
    public StudentSemesterDisciplinePostgresPK getId() {
        return new StudentSemesterDisciplinePostgresPK(
                this.student != null ? this.student.getId() : null,
                this.semester != null ? this.semester.getId() : null,
                this.discipline != null ? this.discipline.getId() : null
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
