package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Group;
import com.itmo.db.generator.model.entity.Person;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class GroupGenerator extends AbstractEntityGenerator<Group, Integer> {

    String getName(Random random, int course) {
        String letter = String.valueOf((char)((int)'A' + random.nextInt(20)));
        return (letter + course) + (100 + random.nextInt(900));
    }

    int getCourse(Random random) {
        return 1 + random.nextInt(4);
    }

    public Calendar getStartDate(Random random) {
        Calendar startDate = new GregorianCalendar(1993, Calendar.SEPTEMBER,1);
        int MAX_YEARS_SINCE_START_DATE = 27;
        startDate.add(Calendar.DAY_OF_MONTH, random.nextInt(MAX_YEARS_SINCE_START_DATE));
        return startDate;
    }

    public Calendar getEndDate(Calendar startDate) {
        Calendar endDate = new GregorianCalendar(1993, Calendar.AUGUST,31);
        endDate.set(Calendar.YEAR, endDate.get(Calendar.YEAR) + 1);
        return endDate;
    }
    public GroupGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Group.class, deps, generator);
    }

    @Override
    protected List<Group> getEntities() {
        log.debug("Creating Group");
        Random random = new Random();

        int course = getCourse(random);
        Calendar startDate = getStartDate(random);
        Calendar endDate = getEndDate(startDate);

        return List.of(new Group(
                null, getName(random, course), String.valueOf(course) + " курс", startDate.getTime(), endDate.getTime()
        ));
    }
}
