package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Dormitory;
import com.itmo.db.generator.model.entity.Room;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
public class RoomGenerator extends AbstractEntityGenerator<Room, Integer> {
    public RoomGenerator(EntityDefinition<Room, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    public int getRoomNumber(Random random) {
        return 1 + random.nextInt(501);
    }

    public short getCapacity(Random random) {
        return (short) (2 + random.nextInt(5));
    }

    public short getEngaged(Random random, short capacity) {
        return (short) (capacity - random.nextInt(3));
    }

    public LocalDate getDate(Random random) {
        LocalDate date = LocalDate.of(1993, Month.JANUARY, 1);
        int MAX_DAYS_SINCE_START_DATE = 26 * 365;
        date = date.plusDays(random.nextInt(MAX_DAYS_SINCE_START_DATE));
        return date;
    }

    private Room getEntity(Dormitory dormitory) {
        short capacity = getCapacity(random);
        boolean bugs = random.nextBoolean();

        return new Room(
                null, getRoomNumber(random), capacity, getEngaged(random, capacity),
                bugs, getDate(random), dormitory.getId()
        );
    }

    @Override
    protected List<Room> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Generating Room");

        return this.getDependencyInstances(Dormitory.class).stream().map(this::getEntity).collect(Collectors.toList());
    }
}

