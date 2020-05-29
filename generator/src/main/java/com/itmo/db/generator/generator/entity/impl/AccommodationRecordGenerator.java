package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.AccommodationRecord;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Room;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class AccommodationRecordGenerator extends AbstractEntityGenerator<AccommodationRecord, Integer> {

    String getCourse(Random random) {
        return (1 + random.nextInt(4)) + "курс";
    }

    public Double getPayment(Random random) {
        return random.nextDouble() * 8000 + 2000;
    }

    public Calendar getLivingStartDate(Random random) {
        Calendar startDate = new GregorianCalendar(2015, Calendar.SEPTEMBER, 1);
        int MAX_YEARS_SINCE_START_DATE = 4;
        startDate.add(Calendar.DAY_OF_MONTH, random.nextInt(MAX_YEARS_SINCE_START_DATE));
        return startDate;
    }

    public Calendar getLivingEndDate(Calendar startDate) {
        Calendar endDate = new GregorianCalendar(2015, Calendar.AUGUST,31);
        endDate.set(Calendar.YEAR, endDate.get(Calendar.YEAR) + 1);
        return endDate;
    }

    public AccommodationRecordGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(AccommodationRecord.class, deps, generator);
    }

    @Override
    protected List<AccommodationRecord> getEntities() {
        log.debug("Creating AccommodationRecord");
        Random random = new Random();

        Person person = this.getDependencyInstances(Person.class).get(0);
        Room room = this.getDependencyInstances(Room.class).get(0);
        Calendar startDate = getLivingStartDate(random);
        Double payment = getPayment(random);

        return List.of(new AccommodationRecord(
                null, person.getId(), room.getId(), random.nextBoolean(), random.nextBoolean(), payment,
                startDate.getTime(), getLivingEndDate(startDate).getTime(), getCourse(random)
        ));
    }
}

