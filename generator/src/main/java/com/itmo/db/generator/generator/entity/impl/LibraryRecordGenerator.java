package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.LibraryRecord;
import com.itmo.db.generator.model.entity.Person;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Random;

public class LibraryRecordGenerator extends AbstractEntityGenerator<LibraryRecord, Integer> {

    public LibraryRecordGenerator(EntityDefinition<LibraryRecord, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    public String getBookId(Random random) {
        StringBuilder bookId = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            bookId.append((char) ((int) 'A' + random.nextInt(20)));
        }
        return bookId.toString();
    }

    public String getAction(Random random) {
        return random.nextBoolean() ? "взял" : "вернул";
    }

    public LocalDate getDate(Random random) {
        LocalDate date = LocalDate.of(2000, Month.JANUARY, 1);
        int MAX_DAYS_SINCE_START_DATE = 7200;
        date = date.plusDays(random.nextInt(MAX_DAYS_SINCE_START_DATE));
        return date;
    }

    @Override
    protected List<LibraryRecord> getEntities() {
        Random random = new Random();
        Person person = this.getDependencyInstances(Person.class).get(0);

        return List.of(new LibraryRecord(null, person.getId(), getBookId(random), getAction(random), getDate(random)));
    }
}
