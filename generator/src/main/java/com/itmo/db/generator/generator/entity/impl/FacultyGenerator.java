package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Faculty;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.Random;

@Slf4j
public class FacultyGenerator extends AbstractEntityGenerator<Faculty, Integer> {
    String[] faculties = new String[] {
            "Институт международного развития и партнерства", "Факультет систем управления и робототехники",
            "Факультет безопасности информационных технологий", "Факультет технологического менеджмента и инноваций",
            "Факультет инфокоммуникационных технологий", "Факультет фотоники и оптоинформатики",
            "Факультет информационных технологий и программирования", "Физико-технический факультет",
            "Факультет лазерной фотоники и оптоэлектроники", "Институт дизайна и урбанистики",
            "Факультет низкотемпературной энергетики", "Институт международного развития и партнерства",
            "Факультет пищевых биотехнологий и инженерии", "Факультет безопасности информационных технологий",
            "Факультет прикладной оптики", "Факультет информационных технологий и программирования",
            "Факультет программной инженерии и компьютерной техники", "Факультет инфокоммуникационных технологий",
            "Факультет лазерной фотоники и оптоэлектроники", "Факультет низкотемпературной энергетики",
            "Факультет пищевых биотехнологий и инженерии", "Факультет программной инженерии и компьютерной техники",
            "Факультет прикладной оптики", "Факультет систем управления и робототехники",
            "Факультет технологического менеджмента и инноваций", "Факультет фотоники и оптоинформатики",
            "Факультет цифровых трансформаций", "Физико-технический факультет", "Химико-биологический кластер",
            "Факультет прикладной оптики", "Военный учебный центр", "Факультет среднего профессионального образования",
    };

    public FacultyGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Faculty.class, deps, generator);
    }

    @Override
    protected Faculty getEntity() {
        log.debug("Creating Faculty");
        Random random = new Random();

        return new Faculty(null, faculties[random.nextInt(faculties.length)]);
    }

}
