package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
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
        double res = random.nextGaussian() * 10.0 + 65.0;
        if (res < 0.0) {
            res = 0.0;
        }
        if (res > 100.0) {
            res = 100.0;
        }
        return (int) Math.floor(res);
    }

    private LocalDate getScoreDate(Integer score, Random random) {
        var m = random.nextInt(8) + 2;
        if (m > 4) {
            m += 2;
        }
        return LocalDate.of(2015 + random.nextInt(4), m, random.nextInt(27) + 1);
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
                                                Professor professor) {
        Integer score = getScore(random);
        LocalDate scoreDate = getScoreDate(score, random);
        Short mark = getMark(score, random);
        Character markChar = score == null ? null : getMarkChar(score, mark);
        LocalDate markDate = mark == null ? null : getScoreDate(mark.intValue(), random);
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
        return this.getDependencyInstances(Professor.class).stream().map(
                professor -> this.getDependencyInstances(Discipline.class).stream().map(
                        discipline -> this.getDependencyInstances(Semester.class).stream().map(
                                semester -> this.getDependencyInstances(Student.class).stream().filter(x -> random.nextBoolean()).map(
                                        student -> this.getEntity(student, discipline, semester, professor)
                                )
                        ).reduce(Stream::concat).orElseThrow()
                ).reduce(Stream::concat).orElseThrow()
        ).reduce(Stream::concat).orElseThrow()
                .collect(Collectors.toList());
    }
}

