package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Group;
import com.itmo.db.generator.model.entity.Person;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class GroupGenerator extends AbstractEntityGenerator<Group, Integer> {

    String getName(Random random) {
        String[] maleNames = new String[] {
                "Aleksandr", "Andrew", "Boris", "Vadim", "Georgy", "Daniil", "Egor", "Zahar", "Ivan", "Kirill", "Lev",
                "Konstantin", "Maxim", "Mikhail", "Nikita", "Oleg", "Petr", "Sergey", "Stepan", "Fedor", "Yaroslav",
        };
        String[] femaleNames = new String[] {
                "Anna", "Alina", "Valentina", "Vera", "Galina", "Darya", "Yana", "Elena", "Zhanna", "Irina", "Lyubov",
                "Lyudmila", "Margarita", "Maria", "Nadezhda", "Natalya", "Polina", "Svetlana", "Sofia", "Tatiana",
        };
        return maleNames[random.nextInt(maleNames.length)];
    }

    String getCourse(Random random) {
        String[] roles = new String[] {"docent", "master", "bachelor"};
        int[] ratios = new int[] {10, 40, 100}; // 10%, 30%, 60%
        int role = random.nextInt(100);

        for (int i = 0; i < roles.length; i++) {
            if (role < ratios[i]) {
                return roles[i];
            }
        }
        return roles[0];
    }

    public Calendar getStartDate(Random random) {
        Calendar startDate = new GregorianCalendar(1993, Calendar.SEPTEMBER,1);
        int MAX_YEARS_SINCE_START_DATE = 27;
        startDate.add(Calendar.DAY_OF_MONTH, random.nextInt(MAX_YEARS_SINCE_START_DATE));
        return startDate;
    }

    public Calendar getEndDate(Calendar startDate) {
        Calendar endDate = new GregorianCalendar(1993, Calendar.AUGUST,31);
        endDate.set(Calendar.YEAR, endDate.get(Calendar.YEAR) + 1);
        return endDate;
    }
    public GroupGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Group.class, deps, generator);
    }

    @Override
    protected Group getEntity() {
        log.debug("Creating Person");
        Random random = new Random();
        Calendar startDate = getStartDate(random);
        Calendar endDate = getEndDate(startDate);

        return new Group(
                null,
                getName(random),
                getCourse(random),
                startDate.getTime(),
                endDate.getTime()
        );
    }
}
