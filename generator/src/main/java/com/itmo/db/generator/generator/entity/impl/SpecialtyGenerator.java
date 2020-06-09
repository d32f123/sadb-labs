package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Faculty;
import com.itmo.db.generator.model.entity.Specialty;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

@Slf4j
public class SpecialtyGenerator extends AbstractEntityGenerator<Specialty, Integer> {

    public SpecialtyGenerator(EntityDefinition<Specialty, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    String getStudyStandart(Random random) {
        return random.nextBoolean() ? "старый" : "новый";
    }

    String getName(Random random) {
        String[] names = new String[]{
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
        };
        return names[random.nextInt(names.length)];
    }

    @Override
    protected List<Specialty> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Creating Specialty");
        Random random = new Random();
        Faculty faculty = this.getDependencyInstances(Faculty.class).get(0);

        return List.of(new Specialty(null, faculty.getId(), getName(random), getStudyStandart(random)));
    }
}

