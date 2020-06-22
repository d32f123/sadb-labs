package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Publication;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class PublicationGenerator extends AbstractEntityGenerator<Publication, Integer> {

    public PublicationGenerator(EntityDefinition<Publication, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    String getLanguage(Random random) {
        String[] languages = new String[]{"RU", "ENG", "DE"};
        int[] ratios = new int[]{50, 90, 10}; // 50%, 40%, 10%
        int lang = random.nextInt(100);

        for (int i = 0; i < languages.length; i++) {
            if (lang < ratios[i]) {
                return languages[i];
            }
        }
        return languages[0];
    }

    String getName(Random random) {
        String[] names = new String[] { "Article", "Theses" };
        return names[random.nextInt(names.length)];
    }

    Integer getIndex(Random random) {
        return 100000 + random.nextInt(899999);
    }

    private Publication getPublication(Random random, List<Person> persons) {
        LocalDate activeDate = LocalDate.of(2010, 1, 1);
        activeDate = persons.stream().map(Person::getDateOfAppearance).reduce(activeDate, (newDate, x) -> {
            if (newDate == null) {
                return null;
            }
            if (x.isAfter(newDate)) {
                return x;
            }
            return newDate;
        });
        if (activeDate == null) {
            return null;
        }

        return new Publication(
                null,
                getName(random),
                getLanguage(random),
                getIndex(random),
                Timestamp.valueOf(activeDate.plusDays(random.nextInt(365 / 2)).atStartOfDay()),
                persons
        );
    }

    @Override
    protected List<Publication> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Creating Publication");
        Random random = new Random();

        var persons = this.getDependencyInstances(Person.class);
        var noDocents = persons.stream().noneMatch(person -> person.getRole().equals("docent"));
        if (noDocents && random.nextInt(10) < 7) {
            return Collections.emptyList();
        }
        return IntStream.range(0, random.nextInt(4)).mapToObj((i) -> {
            var list = new ArrayList<>(persons);
            Collections.shuffle(list);
            var k = random.nextInt(list.size()) + 1;
            return getPublication(random, list.subList(0, k));
        }).filter(Objects::nonNull).collect(Collectors.toUnmodifiableList());

    }
}
