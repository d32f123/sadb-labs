package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ItmoEntity(description = "Student Semester Discipline mapping")
public class StudentSemesterDiscipline implements AbstractEntity<StudentSemesterDiscipline.StudentSemesterDisciplinePK>,
        OracleEntity {

    @ItmoAttribute
    private StudentSemesterDisciplinePK id;

    private Integer professorId;
    private Integer semesterCounter;
    private Integer score;
    private LocalDate scoreDate;
    @ItmoAttribute
    private Short mark;
    @ItmoAttribute
    private Character markLetter;
    @ItmoAttribute
    private LocalDate markDate;

    @Override
    public String getName() {
        return this.toString();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ItmoEntity(description = "Student Semester Discipline Key mapping")
    public static class StudentSemesterDisciplinePK implements OracleEntity {
        @ItmoAttribute
        @ItmoReference(Student.class)
        public Integer studentId;
        @ItmoAttribute
        @ItmoReference(Discipline.class)
        public Integer disciplineId;
        @ItmoAttribute
        @ItmoReference(Semester.class)
        public Integer semesterId;

        @Override
        public String getName() {
            return this.toString();
        }
    }
}
