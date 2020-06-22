package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.LibraryRecord;
import com.itmo.db.generator.model.entity.Person;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
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

    private LibraryRecord getEntity(Person person, int i) {
        if (random.nextInt(10) < 5) {
            return null;
        }
        int years = i / 3;
        var date = person.getDateOfAppearance().plusYears(years).plusDays(random.nextInt(365 - 1));
        return new LibraryRecord(
                null,
                person.getId(),
                getBookId(random),
                getAction(random),
                date
        );
    }

    @Override
    protected List<LibraryRecord> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Generating LibraryRecord");

        var person = this.getDependencyInstances(Person.class).get(0);
        return IntStream.range(0, 12).mapToObj(i -> getEntity(person, i))
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

}
