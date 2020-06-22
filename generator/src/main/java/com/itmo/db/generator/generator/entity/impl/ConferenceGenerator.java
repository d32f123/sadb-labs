package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Conference;
import com.itmo.db.generator.model.entity.Publication;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class ConferenceGenerator extends AbstractEntityGenerator<Conference, Integer> {

    public ConferenceGenerator(EntityDefinition<Conference, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    String getName(Random random) {
        String[] names = new String[]{
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

    private Conference getConference(Random random, List<Publication> publications) {
        var startDate = publications.stream().map(pub -> pub.getDate().toInstant()).reduce(
                LocalDate.of(2000, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC),
                (acc, x) -> {
                    if (acc.isBefore(x)) {
                        return x;
                    }
                    return acc;
                }
        );

        return new Conference(
                null,
                getName(random),
                getLocation(random),
                Timestamp.valueOf(startDate.plus(Duration.ofDays(random.nextInt(365 / 2))).atZone(ZoneOffset.UTC).toLocalDateTime()),
                publications
        );
    }

    @Override
    protected List<Conference> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Creating Conference");
        Random random = new Random();

        return IntStream.range(0, 3).mapToObj(i -> {
            if (random.nextInt(10) < (10 - i * 3)) {
                return null;
            }
            return getConference(random, this.getDependencyInstances(Publication.class));
        }).filter(Objects::nonNull).collect(Collectors.toUnmodifiableList());
    }
}
