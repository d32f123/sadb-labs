package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Dormitory;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DormitoryGenerator extends AbstractEntityGenerator<Dormitory, Integer> {

    public DormitoryGenerator(EntityDefinition<Dormitory, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    public static Queue<String> addresses = Arrays.stream(new String[]{
            "г. Санкт-Петербург, пер. Вяземский, д. 5/7", "г. Санкт-Петербург, ул. Ленсовета, д. 23, лит. А",
            "г. Санкт-Петербург, Альпийский пер., д.15, к. 2, лит. А", "г. Санкт-Петербург, ул. Белорусская, д. 6, лит. А",
            "г. Санкт-Петербург, пр. Новоизмайловский, д. 16", "г. Санкт-Петербург, наб. р. Карповки, д. 22, к. 2, лит. Б",
            "г. Санкт-Петербург, ул. Красного Текстильщика, 13, лит. А",
            "г. Санкт-Петербург, Институтский пр., д.4, к.3", "г. Санкт-Петербург, ул. Новороссийская, д. 28",
            "г. Санкт-Петербург, ул. Новороссийская, д. 26", "г. Санкт-Петербург, ул. Вавиловых, д. 12, к.2",
            "г. Санкт-Петербург, ул. Академика Константинова, д. 6, к.2"
    }).collect(Collectors.toCollection(ArrayDeque::new));

    public String getAddress() {
        return addresses.poll();
    }

    public int getRoomCount(Random random) {
        return 700 + random.nextInt(200);
    }

    @Override
    protected List<Dormitory> getEntities() {
        log.debug("Creating Dormitory");
        Random random = new Random();

        return List.of(new Dormitory(null, getAddress(), getRoomCount(random)));
    }
}

