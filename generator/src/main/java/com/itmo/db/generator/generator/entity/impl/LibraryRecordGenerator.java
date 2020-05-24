package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.*;

import java.util.*;

public class LibraryRecordGenerator extends AbstractEntityGenerator<LibraryRecord, Integer> {
    public String getBookId(Random random) {
        String bookId = "";
        for (int i = 0; i < 8; i++) {
            bookId += String.valueOf((char)((int)'A' + random.nextInt(20)));
        }
        return bookId;
    }

    public String getAction(Random random) {
        return random.nextBoolean() ? "взял" : "вернул";
    }

    public Date getDate(Random random) {
        Calendar date = new GregorianCalendar(2000, Calendar.JANUARY,0);
        int MAX_DAYS_SINCE_START_DATE = 7200;
        date.add(Calendar.DAY_OF_MONTH, random.nextInt(MAX_DAYS_SINCE_START_DATE));
        return date.getTime();
    }

    public LibraryRecordGenerator(Set<DependencyDefinition<?, ?>> dependencies, Generator generator) {
        super(LibraryRecord.class, dependencies, generator);
    }

    @Override
    protected List<LibraryRecord> getEntities() {
        Random random = new Random();
        Person person = this.getDependencyInstances(Person.class).get(0);

        return List.of(new LibraryRecord(null, person.getId(), getBookId(random), getAction(random), getDate(random)));
    }
}
