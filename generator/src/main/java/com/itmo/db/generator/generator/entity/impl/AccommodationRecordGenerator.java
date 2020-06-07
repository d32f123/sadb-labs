package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.AccommodationRecord;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Room;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class AccommodationRecordGenerator extends AbstractEntityGenerator<AccommodationRecord, Integer> {

    public AccommodationRecordGenerator(EntityDefinition<AccommodationRecord, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    Random random = new Random();

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

    private AccommodationRecord getPersonEntity(Person person) {
        Room room = this.getDependencyInstances(Room.class).get(0);
        LocalDate startDate = getLivingStartDate(random);
        Double payment = getPayment(random);

        return new AccommodationRecord(
                null, person.getId(), room.getId(), random.nextBoolean(), random.nextBoolean(), payment,
                startDate, getLivingEndDate(startDate), getCourse(random)
        );
    }

    private Stream<AccommodationRecord> getPersonEntities(Person person) {
        int n = random.nextInt(3) + 1;
        log.debug("Creating '{}' AccommodationRecords", n);
        return IntStream.range(0, n).mapToObj((i) -> this.getPersonEntity(person));
    }

    @Override
    protected List<AccommodationRecord> getEntities() {
        return this.getDependencyInstances(Person.class)
                .stream()
                .map(this::getPersonEntities)
                .reduce(Stream::concat)
                .orElse(Stream.of())
                .collect(Collectors.toList());
    }
}

