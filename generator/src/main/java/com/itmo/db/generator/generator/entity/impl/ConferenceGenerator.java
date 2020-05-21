package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Conference;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
public class ConferenceGenerator extends AbstractEntityGenerator<Conference, Integer> {
    String getName(Random random) {
        String[] names = new String[] {
                "Международная научно-практическая конференция «Российская наука в современном мире»",
                "Международная научно-практическая конференция «Современная наука: актуальные вопросы, достижения и инновации»",
                "Международная научно-практическая конференция «Научно-практические исследования: прикладные науки»",
                "Международный научно-практический форум «ASU SciTech Forum 2020»",
                "Международный научно-практический форум по социальным и поведенческим наукам",
                "Международная научная конференция «Приоритетные направления инновационной деятельности в промышленности»",
                "Международная научно-практическая конференция «Глобальная экономика в XXI веке: роль биотехнологий и цифровых технологий»",
                "Международная научно-практическая конференция «Вопросы современных научных исследований»",
                "IV European Science Forum",
                "Международная научно-практическая конференция «Фундаментальные и прикладные исследования в современном мире»",
                "Международная научно-практическая конференция «Наука и общество» 24-29 мая 2020 года Лондон (UK)",
                "Международная научно-практическая конференция «От инерции к развитию: научно-инновационное обеспечение сельского хозяйства»",
                "Международная конференция «ASEDU-2020: Перспективы развития естественно-научного, инженерного и цифрового образования»",
                "Международная научно-практическая конференция «Современная наука и ее ресурсное обеспечение: инновационная парадигма»",
                "Международный научно-практический форум по безопасности и сотрудничеству в Евразии",
                "Международный журнал научных публикаций «Colloquium-journal»",
                "Международная конференция «CAMSTech-2020: Современные достижения в области материаловедения и технологий»",
                "Международная научно-практическая конференция «Научные исследования: проблемы и перспективы»",
        };
        return names[random.nextInt(names.length)];
    }

    Timestamp getDate(Random random) {
        String y = "201" + random.nextInt(10);

        Integer i = 1 + random.nextInt(12);
        String m = (i > 9) ? String.valueOf(i) : ("0" + i);

        i = 1 + random.nextInt(28);
        String d = (i > 9) ? String.valueOf(i) : ("0" + i);

        return Timestamp.valueOf(y + "-" + m + "-" + d + " 03:00:00");
    }

    String getLocation(Random random) {
        String[] locations = new String[] { "Moscow", "Saint-Petersburg", "Samara", "New-York", "London", "Paris" };
        int[] ratios = new int[] {40, 60, 80, 88, 95, 100}; // 40%, 20%, 20%, 8%, 7%, 5%
        int location = random.nextInt(100);

        for (int i = 0; i < locations.length; i++) {
            if (location < ratios[i]) {
                return locations[i];
            }
        }
        return locations[0];
    }

    public ConferenceGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Conference.class, deps, generator);
    }

    @Override
    protected List<Conference> getEntities() {
        log.debug("Creating Conference");
        Random random = new Random();

        return List.of(new Conference(null, getName(random), getLocation(random), getDate(random)));
    }
}
