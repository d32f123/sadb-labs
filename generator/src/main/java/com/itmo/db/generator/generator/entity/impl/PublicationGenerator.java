package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Publication;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Slf4j
public class PublicationGenerator extends AbstractEntityGenerator<Publication, Integer> {
    String getLanguage(Random random) {
        String[] languages = new String[] { "RU", "ENG", "DE" };
        int[] ratios = new int[] {50, 90, 10}; // 50%, 40%, 10%
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

    Timestamp getDate(Random random) {
        String y = "201" + random.nextInt(10);

        int i = 1 + random.nextInt(12);
        String m = (i > 9) ? String.valueOf(i) : ("0" + i);

        i = 1 + random.nextInt(28);
        String d = (i > 9) ? String.valueOf(i) : ("0" + i);

        return Timestamp.valueOf(y + "-" + m + "-" + d + " 03:00:00");
    }

    public PublicationGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Publication.class, deps, generator);
    }

    @Override
    protected List<Publication> getEntities() {
        log.debug("Creating Publication");
        Random random = new Random();

        return List.of(new Publication(null, getName(random), getLanguage(random), getIndex(random), getDate(random)));
    }
}
