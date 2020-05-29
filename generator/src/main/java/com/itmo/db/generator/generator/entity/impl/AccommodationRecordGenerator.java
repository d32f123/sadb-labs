package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.AccommodationRecord;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Room;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Slf4j
public class AccommodationRecordGenerator extends AbstractEntityGenerator<AccommodationRecord, Integer> {

    String getCourse(Random random) {
        return (1 + random.nextInt(4)) + "курс";
    }

    public Double getPayment(Random random) {
        return random.nextDouble() * 8000 + 2000;
    }

    public LocalDate getLivingStartDate(Random random) {
        LocalDate startDate = LocalDate.of(2015, Month.SEPTEMBER, 1);
        int MAX_YEARS_SINCE_START_DATE = 4;
        startDate = startDate.plusDays(random.nextInt(MAX_YEARS_SINCE_START_DATE));
        return startDate;
    }

    public LocalDate getLivingEndDate(LocalDate startDate) {
        return LocalDate.of(startDate.getYear() + 1, Month.AUGUST, 31);
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
        LocalDate startDate = getLivingStartDate(random);
        Double payment = getPayment(random);

        return List.of(new AccommodationRecord(
                null, person.getId(), room.getId(), random.nextBoolean(), random.nextBoolean(), payment,
                startDate, getLivingEndDate(startDate), getCourse(random)
        ));
    }
}

