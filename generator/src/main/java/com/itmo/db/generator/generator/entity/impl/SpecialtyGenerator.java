package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Faculty;
import com.itmo.db.generator.model.entity.Specialty;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class SpecialtyGenerator extends AbstractEntityGenerator<Specialty, Integer> {

    public SpecialtyGenerator(EntityDefinition<Specialty, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    private final List<String> names = Arrays.stream(new String[]{
            "01.03.02\tПрикладная математика и информатика", "01.04.02\tПрикладная математика и информатика",
            "02.04.03\tМатематическое обеспечение и администрирование информационных систем", "07.04.04\tГрадостроительство",
            "09.03.01\tИнформатика и вычислительная техника", "09.03.02\tИнформационные системы и технологии",
            "09.03.03\tПрикладная информатика", "09.03.04\tПрограммная инженерия",
            "09.04.01\tИнформатика и вычислительная техника", "09.04.02\tИнформационные системы и технологии",
            "09.04.03\tПрикладная информатика", "09.04.04\tПрограммная инженерия",
            "10.03.01\tИнформационная безопасность", "10.03.01\tИнформационная безопасность",
            "11.03.02\tИнфокоммуникационные технологии и системы связи", "11.03.03\tКонструирование и технология электронных средств",
            "11.04.02\tИнфокоммуникационные технологии и системы связи", "11.04.03\tКонструирование и технология электронных средств",
            "12.03.01\tПриборостроение", "12.03.02\tОптотехника", "12.03.05\tЛазерная техника и лазерные технологии",
            "12.03.03\tФотоника и оптоинформатика", "12.03.04\tБиотехнические системы и технологии", "12.04.01\tПриборостроение",
    }).unordered().collect(Collectors.toUnmodifiableList());
    Random random = new Random();
    private List<String> availableNames;

    String getStudyStandart(Random random) {
        return random.nextBoolean() ? "старый" : "новый";
    }

    String getName() {
        String name = availableNames.get(0);
        availableNames.remove(0);
        return name;
    }

    private Specialty getEntity(Faculty faculty) {
        return new Specialty(null, faculty.getId(), getName(), getStudyStandart(random));
    }

    @Override
    protected List<Specialty> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Creating Specialty");

        availableNames = new LinkedList<>(names);
        Collections.shuffle(availableNames);
        Faculty faculty = this.getDependencyInstances(Faculty.class).get(0);
        return IntStream.range(0, random.nextInt(5) + 1)
                .mapToObj(i -> getEntity(faculty))
                .collect(Collectors.toList());
    }
}

