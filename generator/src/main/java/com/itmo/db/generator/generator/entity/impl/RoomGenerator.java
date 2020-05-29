package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Dormitory;
import com.itmo.db.generator.model.entity.Room;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class RoomGenerator extends AbstractEntityGenerator<Room, Integer> {
    public int getRoomNumber(Random random) {
        return 1 + random.nextInt(501);
    }

    public short getCapacity(Random random) {
        return (short)(2 + random.nextInt(5));
    }

    public short getEngaged(Random random, short capacity) {
        return (short)(capacity - random.nextInt(3));
    }

    public RoomGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Room.class, deps, generator);
    }

    public Date getDate(Random random) {
        Calendar date = new GregorianCalendar(2019, Calendar.JANUARY,0);
        int MAX_DAYS_SINCE_START_DATE = 365;
        date.add(Calendar.DAY_OF_MONTH, random.nextInt(MAX_DAYS_SINCE_START_DATE));
        return date.getTime();
    }

    @Override
    protected List<Room> getEntities() {
        log.debug("Creating Room");
        Random random = new Random();

        short capacity = getCapacity(random);
        boolean bugs = random.nextBoolean();
        Dormitory dormitory = this.getDependencyInstances(Dormitory.class).get(0);

        return List.of(new Room(
                null, getRoomNumber(random), capacity, getEngaged(random, capacity),
                bugs, getDate(random), dormitory.getId()
        ));
    }
}

