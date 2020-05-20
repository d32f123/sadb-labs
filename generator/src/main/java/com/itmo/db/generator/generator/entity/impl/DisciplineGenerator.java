package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Discipline;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class DisciplineGenerator extends AbstractEntityGenerator<Discipline, Integer> {

    String getControleForm(Random random) {
        String[] forms = new String[] {"exam", "test"};
        return forms[random.nextInt(forms.length)];
    }

    Integer getHours(Random random) {
        return 10 + random.nextInt(30);
    }

    String getName(Random random) {
        String[] disciplines = new String[] {
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
        };
        return disciplines[random.nextInt(disciplines.length)];
    }


    public DisciplineGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Discipline.class, deps, generator);
    }

    @Override
    protected Discipline getEntity() {
        log.debug("Creating Discipline");
        Random random = new Random();

        return new Discipline(
                null, getName(random), getControleForm(random), getHours(random), getHours(random), getHours(random)
        );
    }
}
