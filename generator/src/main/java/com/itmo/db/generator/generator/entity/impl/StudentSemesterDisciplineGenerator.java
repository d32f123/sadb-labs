package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Slf4j
public class StudentSemesterDisciplineGenerator
        extends AbstractEntityGenerator<StudentSemesterDiscipline, StudentSemesterDiscipline.StudentSemesterDisciplinePK> {

    public StudentSemesterDisciplineGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(StudentSemesterDiscipline.class, deps, generator);
    }

    private int getScore(Random random) {
        double res = random.nextGaussian() * 10.0 + 65.0;
        if (res < 0.0) {
            res = 0.0;
        }
        if (res > 100.0) {
            res = 100.0;
        }
        return (int) Math.floor(res);
    }

    private LocalDate getScoreDate(Random random) {
        var m = random.nextInt(8) + 2;
        if (m > 4) {
            m += 2;
        }
        return LocalDate.of(2015 + random.nextInt(4), m, random.nextInt(27) + 1);
    }

    private Short getMark(int score, Random random) {
        if (random.nextInt(10) < 2) {
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
        var score = getScore(random);
        var scoreDate = getScoreDate(random);
        var mark = getMark(score, random);
        var markChar = getMarkChar(score, mark);
        var markDate = getScoreDate(random);

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

