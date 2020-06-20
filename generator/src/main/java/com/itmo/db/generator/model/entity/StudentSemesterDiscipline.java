package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.annotations.MergeKey;
import com.itmo.db.generator.persistence.db.merge.repository.StudentSemesterDisciplineMergeRepository;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ItmoEntity(description = "Student Semester Discipline mapping")
@Entity
@EntityJpaRepository(clazz = StudentSemesterDisciplineMergeRepository.class)
public class StudentSemesterDiscipline implements AbstractEntity<StudentSemesterDiscipline.StudentSemesterDisciplinePK>,
        OracleEntity {

    @ItmoAttribute
    @Id
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
        @ItmoReference(Person.class)
        public Integer studentId;
        @ItmoAttribute
        @ItmoReference(Discipline.class)
        public Integer disciplineId;
        @ItmoAttribute
        @ItmoReference(value = Semester.class, isTransient = true)
        public Integer semesterId;

        @Override
        public String getName() {
            return this.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StudentSemesterDisciplinePK that = (StudentSemesterDisciplinePK) o;
            return studentId.equals(that.studentId) &&
                    disciplineId.equals(that.disciplineId) &&
                    semesterId.equals(that.semesterId);
        }

        @Override
        @MergeKey
        public int hashCode() {
            return Objects.hash(studentId, disciplineId, semesterId);
        }
    }
}
