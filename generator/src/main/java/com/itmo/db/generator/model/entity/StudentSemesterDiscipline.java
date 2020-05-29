package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private Integer semesterCounter;
    private Integer score;
    private LocalDate scoreDate;
    private Short mark;
    private Character markLetter;
    private LocalDate markDate;

}
