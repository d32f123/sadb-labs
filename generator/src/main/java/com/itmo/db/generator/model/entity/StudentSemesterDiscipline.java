package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class StudentSemesterDiscipline implements AbstractEntity<StudentSemesterDiscipline.StudentSemesterDisciplinePK> {

    private StudentSemesterDisciplinePK id;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentSemesterDisciplinePK {
        public Integer studentId;
        public Integer disciplineId;
        public Integer semesterId;
    }

    private Integer professorId;
    private int semesterCounter;
    private int score;
    private LocalDate scoreDate;
    private short mark;
    private char markLetter;
    private LocalDate markDate;

}
