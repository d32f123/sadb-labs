package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Group;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Slf4j
public class GroupGenerator extends AbstractEntityGenerator<Group, Integer> {

    String getName(Random random, int course) {
        String letter = String.valueOf((char) ((int) 'A' + random.nextInt(20)));
        return (letter + course) + (100 + random.nextInt(900));
    }

    int getCourse(Random random) {
        return 1 + random.nextInt(4);
    }

    public LocalDate getStartDate(Random random) {
        LocalDate startDate = LocalDate.of(1993, Calendar.SEPTEMBER, 1);
        int MAX_YEARS_SINCE_START_DATE = 27;
        startDate = startDate.plusDays(random.nextInt(MAX_YEARS_SINCE_START_DATE));
        return startDate;
    }

    public LocalDate getEndDate(LocalDate startDate) {
        return LocalDate.of(startDate.getYear() + 1, Calendar.AUGUST, 31);
    }

    public GroupGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Group.class, deps, generator);
    }

    @Override
    protected List<Group> getEntities() {
        log.debug("Creating Group");
        Random random = new Random();

        int course = getCourse(random);
        LocalDate startDate = getStartDate(random);
        LocalDate endDate = getEndDate(startDate);

        return List.of(new Group(
                null, getName(random, course), course + " курс", startDate, endDate
        ));
    }
}
