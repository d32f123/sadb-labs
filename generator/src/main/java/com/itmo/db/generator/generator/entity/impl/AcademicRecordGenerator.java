package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.AcademicRecord;
import com.itmo.db.generator.model.entity.AcademicRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class AcademicRecordGenerator extends AbstractEntityGenerator<AcademicRecord, Integer> {
    int budgetRatio = 80;
    int fullTimeRatio = 90;
    int studentRatio = 75;

    public Calendar getAcademicYear(Calendar startDate) {
        return new GregorianCalendar(startDate.get(Calendar.YEAR) + 1, Calendar.SEPTEMBER, 1);
    }

    public String getDegree(Random random) {
        String[] roles = new String[]{"доцент", "магистр", "бакалавр"};
        int[] ratios = new int[]{10, 40, 100}; // 10%, 30%, 60%
        int role = random.nextInt(100);

        for (int i = 0; i < roles.length; i++) {
            if (role < ratios[i]) {
                return roles[i];
            }
        }
        return roles[0];
    }

    public boolean getBudget(Random random) {
        return random.nextInt(100) <= budgetRatio;
    }

    public boolean getFullTime(Random random) {
        return random.nextInt(100) <= fullTimeRatio;
    }

    public String getDirection(Random random) {
        String[] directions = new String[]{
                "01.03.02 - Прикладная математика и информатика", "01.04.02 - Прикладная математика и информатика",
                "02.04.03 - Математическое обеспечение и администрирование информационных систем",
                "07.04.04 - Градостроительство", "09.03.01 - Информатика и вычислительная техника", "09.03.02 - Информационные системы и технологии", "09.03.03 - Прикладная информатика", "09.03.04 - Программная инженерия", "09.04.01 - Информатика и вычислительная техника", "09.04.02 - Информационные системы и технологии", "09.04.03 - Прикладная информатика", "09.04.04 - Программная инженерия", "10.03.01 - Информационная безопасность", "10.04.01 - Информационная безопасность", "11.03.02 - Инфокоммуникационные технологии и системы связи", "11.03.03 - Конструирование и технология электронных средств", "11.04.02 - Инфокоммуникационные технологии и системы связи", "11.04.03 - Конструирование и технология электронных средств", "12.03.01 - Приборостроение", "12.03.02 - Оптотехника", "12.03.03 - Фотоника и оптоинформатика", "12.03.04 - Биотехнические системы и технологии", "12.03.05 - Лазерная техника и лазерные технологии", "12.04.01 - Приборостроение", "12.04.02 - Оптотехника", "12.04.03 - Фотоника и оптоинформатика", "12.04.04 - Биотехнические системы и технологии", "12.04.05 - Лазерная техника и лазерные технологии", "12.05.01 - Электронные и оптико-электронные приборы и системы специального назначения", "13.03.02 - Электроэнергетика и электротехника", "13.04.02 - Электроэнергетика и электротехника", "14.03.01 - Ядерная энергетика и теплофизика", "15.03.02 - Технологические машины и оборудование", "15.03.04 - Автоматизация технологических процессов и производств", "15.03.06 - Мехатроника и робототехника", "15.04.02 - Технологические машины и оборудование", "15.04.04 - Автоматизация технологических процессов и производств", "15.04.06 - Мехатроника и робототехника", "16.03.01. - Техническая физика", "16.03.03 - Холодильная", " криогенная техника и системы жизнеобеспечения", "16.04.01 - Техническая физика", "16.04.03 - Холодильная", " криогенная техника и системы жизнеобеспечения", "18.03.02 - Энерго- и ресурсосберегающие процессы в химической технологии", " нефтехимии и биотехнологии", "18.04.02 - Энерго- и ресурсосберегающие процессы в химической технологии", " нефтехимии и биотехнологии", "19.03.01. - Биотехнология", "19.03.02 - Продукты питания из растительного сырья", "19.03.03 - Продукты питания животного происхождения", "19.04.01 - Биотехнология", "19.04.02 - Продукты питания из растительного сырья", "19.04.03 - Продукты питания животного происхождения", "20.04.01 - Техносферная безопасность", "23.03.03 - Эксплуатация транспортно-технологических машин и комплексов", "23.04.03 - Эксплуатация транспортно-технологических машин и комплексов", "24.03.02 - Системы управления движением и навигация", "24.04.01 - Ракетные комплексы и космонавтика", "24.04.02 - Системы управления движением и навигация", "27.03.04 - Управление в технических системах", "27.03.05 - Инноватика", "27.04.01 - Стандартизация и метрология", "27.04.02 - Управление качеством", "27.04.03 - Системный анализ и управление", "27.04.04 - Управление в технических системах", "27.04.05 - Инноватика", "27.04.07 - Наукоемкие технологии и экономика инноваций", "27.04.08 - Управление интеллектуальной собственностью", "38.03.01 - Экономика", "38.03.02 - Менеджмент", "38.03.05. - Бизнес-информатика", "38.04.01 - Экономика", "38.04.02 - Менеджмент", "38.04.05 - Бизнес-информатика", "38.05.02 - Таможенное дело", "44.03.04 - Профессиональное обучение", "45.03.04 - Интеллектуальные системы в гуманитарной сфере", "45.04.04 - Интеллектуальные системы в гуманитарной среде"
        };
        return directions[random.nextInt(directions.length)];
    }

    public String getSpecialty(Random random) {
        String[] specialities = new String[]{
                "Бизнес-информатика", "Биоинженерия", "Биотехнология", "Иностранные языки и информационные технологии / Humanities and IT", "Интеллектуальные системы в гуманитарной сфере", "Информатика и программирование", "Информационные технологии в энергетике", "Компьютерные системы и технологии", "Компьютерные технологии в дизайне", "Лазерная фотоника и оптоэлектроника", "Мобильные и сетевые технологии", "Нейротехнологии и программирование", "Низкотемпературная техника и энергетика", "Оптико-электронные приборы и системы", "Прикладная и теоретическая физика", "Прикладная оптика", "Программирование в инфокоммуникационных системах", "Программирование и интернет-технологии", "Робототехника", "Системное и прикладное программное обеспечение", "Технологии защиты информации", "Управление технологическими инновациями", "Фотоника и оптоинформатика", "Цифровое производство", "Цифровые системы управления"
        };
        return specialities[random.nextInt(specialities.length)];
    }

    public String getPosition(Random random) {
        String[] roles = new String[]{"Студент", "Аспирант", "Профессор", "Старший преподаватель", "Стажер", "Старший научный сотрудник", "Ассистент", "Ведущий научный сотрудник", "Главный научный сотрудник", "Докторант", "Доцент", "Младший научный сотрудник", "Научный сотрудник", "Преподаватель"};
        if (random.nextInt(100) <= studentRatio)
            return roles[0];
        return roles[random.nextInt(roles.length)];
    }

    public String getSubdivision(Random random) {
        String[] subdivisions = new String[]{
                "Факультет систем управления и робототехники", "Факультет программной инженерии и компьютерной техники", "Факультет безопасности информационных технологий", "Физико-технический факультет", "Факультет фотоники и оптоинформатики", "Факультет прикладной оптики", "Факультет лазерной фотоники и оптоэлектроники", "Факультет информационных технологий и программирования", "Факультет инфокоммуникационных технологий", "Факультет цифровых трансформаций", "Институт трансляционной медицины", "Институт финансовых кибертехнологий", "Институт дизайна и урбанистики", "Факультет низкотемпературной энергетики", "Факультет пищевых биотехнологий и инженерии", "Химико-биологический кластер", "Научно-образовательный центр химического инжиниринга и биотехнологий", "Факультет технологического менеджмента и инноваций", "Факультет среднего профессионального образования", "Высшая школа цифровой культуры"
        };
        return subdivisions[random.nextInt(subdivisions.length)];
    }

    public Calendar getStartDate(Random random) {
        Calendar startDate = new GregorianCalendar(1993, Calendar.SEPTEMBER, 1);
        int MAX_YEARS_SINCE_START_DATE = 27;
        startDate.add(Calendar.DAY_OF_MONTH, random.nextInt(MAX_YEARS_SINCE_START_DATE));
        return startDate;
    }

    public Calendar getEndDate(Calendar startDate) {
        return new GregorianCalendar(startDate.get(Calendar.YEAR) + 1, Calendar.AUGUST, 31);
    }

    public AcademicRecordGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(AcademicRecord.class, deps, generator);
    }

    @Override
    protected List<AcademicRecord> getEntities() {
        log.debug("Creating AcademicRecord");
        Random random = new Random();
        Calendar startDate = getStartDate(random);
        Calendar endDate = getEndDate(startDate);
        Calendar academicYear = getStartDate(random);

        return List.of(new AcademicRecord(
                null,
                null,
                academicYear.getTime(),
                getDegree(random),
                getBudget(random),
                getFullTime(random),
                getDirection(random),
                getSpecialty(random),
                getPosition(random),
                getSubdivision(random),
                startDate.getTime(),
                endDate.getTime()
        ));
    }
}