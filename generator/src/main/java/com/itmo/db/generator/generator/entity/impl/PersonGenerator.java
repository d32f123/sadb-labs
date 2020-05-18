package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.pool.EntityPool;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.Random;

@Slf4j
public class PersonGenerator extends AbstractEntityGenerator<Person, Integer> {
    int maleFemaleRatio = 55;

    String getFirstName(Random random, Boolean isMale) {
        String[] maleNames = new String[] {
                "Aleksandr", "Andrew", "Boris", "Vadim", "Georgy", "Daniil", "Egor", "Zahar", "Ivan", "Kirill", "Lev",
                "Konstantin", "Maxim", "Mikhail", "Nikita", "Oleg", "Petr", "Sergey", "Stepan", "Fedor", "Yaroslav",
        };
        String[] femaleNames = new String[] {
                "Anna", "Alina", "Valentina", "Vera", "Galina", "Darya", "Yana", "Elena", "Zhanna", "Irina", "Lyubov",
                "Lyudmila", "Margarita", "Maria", "Nadezhda", "Natalya", "Polina", "Svetlana", "Sofia", "Tatiana",
        };
        return (isMale) ? maleNames[random.nextInt(maleNames.length)] : femaleNames[random.nextInt(femaleNames.length)];
    }

    String getSurname(Random random, Boolean isMale) {
        String[] surnames = new String[] {
                "Belogolov", "Bykov", "Grishin", "Kulikov", "Fomin", "Mileshin", "Muratov", "Turov", "Norin", "Noskov",
                "Pnachin", "Romanov", "Tomilov", "Tumaykin", "Nesterov", "Yunusov", "Rozhkin", "Aliev", "Dorokhov",
                "Korolev", "Matesov", "Mileev", "Novikov", "Olyunin", "Ivanov", "Atepaev", "Petrov", "Loginov",
        };
        String surname = surnames[random.nextInt(surnames.length)];
        return (isMale) ? surname : surname + "a";
    }

    String getPatronymic(Random random, Boolean isMale) {
        String[] patronymics = new String[] {
                "Olegov", "Vladimirov", "Denisov", "Anatol'yev", "Yur'yev", "Sergeev", "Amayakov", "Igorev", "Andreev",
                "Dmitriev", "Rustamov", "Leonidov", "Pavlov", "Varsonof'yev", "Rafailov", "Vital'yev", "Aleksandrov",
                "Vasil'yev", "Stepnov", "Petrov", "Konstantinov", "Yaroslavov", "Maximov", "Zaharov", "Borisov",
        };
        String surname = patronymics[random.nextInt(patronymics.length)];
        return (isMale) ? surname + "ich" : surname + "na";
    }

    String getRole(Random random) {
        int studentTeacherRatio = 80;
        return random.nextInt(100) < studentTeacherRatio ? "student" : "teacher";
    }

    public PersonGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Person.class, deps, generator);
    }

    @Override
    protected Person getEntity() {
        log.debug("Creating Person");
        Random random = new Random();
        Boolean isMale = random.nextInt(100) <= maleFemaleRatio;

        return new Person(
                null, getFirstName(random, isMale), getSurname(random, isMale),
                getPatronymic(random, isMale), getRole(random)
        );
    }
}
