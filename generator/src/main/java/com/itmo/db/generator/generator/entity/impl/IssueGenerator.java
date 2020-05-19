package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Issue;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.Set;

@Slf4j
public class IssueGenerator extends AbstractEntityGenerator<Issue, Integer> {

    String getName(Random random) {
        String[] part_1 = new String[] { "Исследование", "Разработка", "Модификация", "Улучшение" };
        String[] part_2 = new String[] { "алгоритма", "метода", "способа" };
        String[] part_3 = new String[] { "поиска", "удаления", "перемещения", "модификации" };
        String[] part_4 = new String[] {
                "ошибок", "букв", "слов", "фраз", "предложений", "омонимов", "синонимов", "антонимов", "фразеологизмов",
                "абзацев", "слогов",
        };
        String[] part_5 = new String[] {
                "в книге Гарри Поттера", "в алгоритме сортировки Пузырьком", "в последнем сообщении в ВК",
                "в устной речи", "в газете", "в научной статье", "в WhatsApp", "в случайной фразе",
        };

        return part_1[random.nextInt(part_1.length)] + " " + part_2[random.nextInt(part_2.length)] + " " +
                part_3[random.nextInt(part_3.length)] + " " + part_4[random.nextInt(part_4.length)] + " " +
                part_5[random.nextInt(part_5.length)];
    }

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

    String getLocation(Random random) {
        String[] locations = new String[] { "Moscow", "Tver" };
        return locations[random.nextInt(locations.length)];
    }

    String getFormat(Random random) {
        String[] locations = new String[] {
                "ВАК", "РИНЦ", "Scopus", "WoS", "European Reference Index for the Humanities",
        };
        return locations[random.nextInt(locations.length)];
    }

    int getLength(Random random) {
        return 5 + random.nextInt(5);
    }

    public IssueGenerator(Set<DependencyDefinition<?, ?>> dependencies, Generator generator) {
        super(Issue.class, dependencies, generator);
    }

    @Override
    protected Issue getEntity() {
        log.debug("Creating Issue");
        Random random = new Random();

        return new Issue(null, getName(random), getLanguage(random), getLocation(random), getLength(random), getFormat(random));
    }
}