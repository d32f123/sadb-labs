package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Discipline;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DisciplineGenerator extends AbstractEntityGenerator<Discipline, Integer> {

    public DisciplineGenerator(EntityDefinition<Discipline, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    String getControleForm(Random random) {
        String[] forms = new String[]{"exam", "test"};
        return forms[random.nextInt(forms.length)];
    }

    Integer getHours(Random random) {
        return 10 + random.nextInt(30);
    }

    public static Queue<String> disciplines = Arrays.stream(new String[]{
            "Введение в специальность", "Интеллектуальная собственность", "Информатика и программирование",
            "Метрология, стандартизация и сертификация", "Пакеты прикладных программ", "Теория колебаний",
            "Информатика", "Теория вероятностей и математическая статистика", "Технологии обработки конструкционных материалов",
            "Электроника и микропроцессорная техника", "Испытание навигационных систем и программы обработки экспериментальных данных",
            "Математические основы теории оценивания", "Прикладная теория гироскопов", "Программирование и основы алгоритмизации",
            "Теоретические основы навигации и управления", "Аппаратные средства цифровой обработки информации",
            "Моделирование систем управления", "Принципы построения приборов ориентации", "Автоматизация проектирования гироскопических приборов (механика)",
            "Инерциальные навигационные системы", "Инерциальные чувствительные элементы", "Оптические гироскопы",
            "Цифровая обработка сигналов", "Спутниковые навигационные приборы", "Технология изготовления приборов",
            "Интегрированные системы ориентации и навигации", "Надёжность и диагностика информационных навигационных систем",
            "Методы и задачи обработки навигационной информации", "Интеллектуальные технологии в задачах управления и навигации",
            "Прикладное программирование в информационно-навигационных системах", "Методы и системы подземной навигации нефтегазовых и рудных скважин",
            "Геоинформационные навигационные системы и технологии", "Интегрированные инерциально-спутниковые системы ориентации и навигации",
            "Интеллектуальные методы проектирования инерциальных ЧЭ ИНС", "Формирование программы реального времени для навигационной системы в пакете MATLAB"
    }).collect(Collectors.toCollection(ArrayDeque::new));

    String getName() {
        return disciplines.poll();
    }

    @Override
    protected List<Discipline> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Creating Discipline");
        Random random = new Random();

        return List.of(new Discipline(
                null, getName(), getControleForm(random), getHours(random), getHours(random), getHours(random)
        ));
    }
}
