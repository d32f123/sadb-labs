package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class StudentSemesterDisciplineGenerator
        extends AbstractEntityGenerator<StudentSemesterDiscipline, StudentSemesterDiscipline.StudentSemesterDisciplinePK> {

    public StudentSemesterDisciplineGenerator(EntityDefinition<StudentSemesterDiscipline, StudentSemesterDiscipline.StudentSemesterDisciplinePK> entity, Generator generator) {
        super(entity, generator);
    }

    private Integer getScore(Random random) {
        if (random.nextInt(10) < 1) {
            return 0;
        }
        double res = random.nextGaussian() * 15.0 + 72.0;
        if (res < 0.0) {
            res = 0.0;
        }
        if (res > 100.0) {
            res = 100.0;
        }
        return (int) Math.floor(res);
    }

    private LocalDate getScoreDate(int year, boolean secondSemester, Random random) {
        LocalDate date = LocalDate.of(year, secondSemester ? 2 : 9, 1);
        return date.plusDays(random.nextInt(90));
    }

    private Short getMark(Integer score, Random random) {
        if (score < 30 || random.nextInt(10) < 2) {
            return null;
        }
        if (score < 60) {
            return 2;
        }
        if (score < 75) {
            return 3;
        }
        if (score < 90) {
            return 4;
        }
        return 5;
    }

    private Character getMarkChar(int score, Short mark) {
        if (mark == null) {
            return null;
        }
        if (score < 60) {
            return 'F';
        }
        if (score < 70) {
            return 'E';
        }
        if (score < 75) {
            return 'D';
        }
        if (score < 80) {
            return 'C';
        }
        if (score < 90) {
            return 'B';
        }
        return 'A';
    }

    private final Random random = new Random();
    private int counter = 0;

    private StudentSemesterDiscipline getEntity(Student student,
                                                Discipline discipline,
                                                Semester semester,
                                                Professor professor,
                                                int year) {
        var secondSemester = semester.getId() % 2 == 0;
        Integer score = getScore(random);
        LocalDate scoreDate = getScoreDate(year, secondSemester, random);
        Short mark = getMark(score, random);
        Character markChar = score == null ? null : getMarkChar(score, mark);
        LocalDate markDate = mark == null ? null : scoreDate;
        counter++;

        return new StudentSemesterDiscipline(
                new StudentSemesterDiscipline.StudentSemesterDisciplinePK(
                        student.getId(),
                        discipline.getId(),
                        semester.getId()
                ),
                counter,
                professor.getId(),
                1,
                score,
                scoreDate,
                mark,
                markChar,
                markDate
        );
    }

    @Override
    protected List<StudentSemesterDiscipline> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Creating StudentSemesterDiscipline");
        var professors = this.getDependencyInstances(Professor.class);
        var disciplines = this.getDependencyInstances(Discipline.class);
        var semesters = this.getDependencyInstances(Semester.class);
        var students = this.getDependencyInstances(Student.class);

        var minYear = students.stream()
                .map(Student::getPerson)
                .map(Person::getDateOfAppearance)
                .reduce(
                        LocalDate.of(2010, 12, 31),
                        (acc, x) -> {
                            if (acc.isAfter(x)) {
                                return x;
                            }
                            return acc;
                        }
                ).getYear();
        var maxYear = students.stream()
                .map(Student::getPerson)
                .map(person -> person.getDateOfAppearance().plusYears(4))
                .reduce(
                        LocalDate.of(2000, 1, 1),
                        (acc, x) -> {
                            if (acc.isBefore(x)) {
                                return x;
                            }
                            return acc;
                        }
                ).getYear();

        return IntStream.range(minYear, maxYear + 1).mapToObj(year ->
                students.stream()
                        .filter(student -> student.getPerson().getDateOfAppearance().getYear() < year &&
                                student.getPerson().getDateOfAppearance().getYear() + 4 > year)
                        .map((student) -> {
                            var professor = professors.get(random.nextInt(professors.size()));
                            return semesters.stream().map(semester ->
                                disciplines.stream().map(discipline ->
                                    getEntity(student, discipline, semester, professor, year)
                                )
                            );
                        }).reduce(Stream.empty(), Stream::concat)
                        .reduce(Stream.empty(), Stream::concat)
        ).reduce(Stream.empty(), Stream::concat).collect(Collectors.toUnmodifiableList());

    }
}

