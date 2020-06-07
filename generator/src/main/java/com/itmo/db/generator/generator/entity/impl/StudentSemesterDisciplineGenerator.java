package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

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

    @Override
    protected List<StudentSemesterDiscipline> getEntities() {
        log.debug("Creating StudentSemesterDiscipline");
        var random = new Random();
        Integer score = getScore(random);
        LocalDate scoreDate = getScoreDate(score, random);
        Short mark = getMark(score, random);
        Character markChar = score == null ? null : getMarkChar(score, mark);
        LocalDate markDate = mark == null ? null : getScoreDate(mark.intValue(), random);

        var entity = new StudentSemesterDiscipline(
                new StudentSemesterDiscipline.StudentSemesterDisciplinePK(
                        this.getDependencyInstances(Student.class).get(0).getId(),
                        this.getDependencyInstances(Discipline.class).get(0).getId(),
                        this.getDependencyInstances(Semester.class).get(0).getId()
                ),
                this.getDependencyInstances(Professor.class).get(0).getId(),
                1,
                score,
                scoreDate,
                mark,
                markChar,
                markDate
        );

        return List.of(entity);
    }
}

