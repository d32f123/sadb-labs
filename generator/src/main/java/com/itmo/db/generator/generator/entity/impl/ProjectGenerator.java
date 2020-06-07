package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Project;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

@Slf4j
public class ProjectGenerator extends AbstractEntityGenerator<Project, Integer> {

    public ProjectGenerator(EntityDefinition<Project, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    String getName(Random random) {
        String[] part_1 = new String[]{"Исследование", "Разработка", "Модификация", "Улучшение", "Оптимизация"};
        String[] part_2 = new String[]{"алгоритма", "метода", "способа"};
        String[] part_3 = new String[]{"поиска", "удаления", "перемещения", "модификации", "замены"};
        String[] part_4 = new String[]{
                "ошибок", "букв", "слов", "фраз", "предложений", "омонимов", "синонимов", "антонимов", "фразеологизмов",
                "абзацев", "слогов", "комплиментов", "оскорблений"
        };
        String[] part_5 = new String[]{
                "в книгах Клайва Льюиса", "в алгоритме построения графа", "в методичке по базам данных",
                "в устной речи", "в журнале", "в паспорте научного руководителя", "в Telegram'е", "в случайной статье",
        };

        return part_1[random.nextInt(part_1.length)] + " " + part_2[random.nextInt(part_2.length)] + " " +
                part_3[random.nextInt(part_3.length)] + " " + part_4[random.nextInt(part_4.length)] + " " +
                part_5[random.nextInt(part_5.length)];
    }

    @Override
    protected List<Project> getEntities() {
        log.debug("Creating Project");

        return List.of(new Project(null, getName(new Random())));
    }
}
