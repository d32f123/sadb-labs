package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Person;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Random;

@Slf4j
public class PersonGenerator extends AbstractEntityGenerator<Person, Integer> {
    int maleFemaleRatio = 55;
    private int personNumber = 1;

    public PersonGenerator(EntityDefinition<Person, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    String getFirstName(Random random, Boolean isMale) {
        String[] maleNames = new String[]{
                "Aleksandr", "Andrew", "Boris", "Vadim", "Georgy", "Daniil", "Egor", "Zahar", "Ivan", "Kirill", "Lev",
                "Konstantin", "Maxim", "Mikhail", "Nikita", "Oleg", "Petr", "Sergey", "Stepan", "Fedor", "Yaroslav",
        };
        String[] femaleNames = new String[]{
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

    public LocalDate getBirthDate(Random random, String role) {
        LocalDate date = LocalDate.of(1975, Month.JANUARY, 1);
        int maxDays = 27 * 365;
        date = date.plusDays(random.nextInt(maxDays));
        switch (role){
            case "docent":
                date = date.minusYears(5);
                date = date.minusYears(random.nextInt(30));
                break;
            case "master":
                date = date.minusYears(4);
                date = date.minusYears(random.nextInt(5));
                break;
            case "bachelor":
                date = date.minusYears(random.nextInt(5));
                break;
        }
        return date;
    }

    public String getPersonNumber() {
        return String.format("s%05d", personNumber++);
    }

    public String getBirthPlace(Random random) {
        String[] cities = new String[]{
                "Moscow", "Saint-Petersburg", "Abakan", "Azov", "Aleksandrov", "Aleksin", "Al'met'evsk", "Anapa", "Angarsk",
                "Anzhero-Sudzhensk", "Apatity", "Arzamas", "Armavir", "Arsen'ev", "Artem", "Arhangel'sk", "Asbest", "Astrahan'",
                "Achinsk", "Balakovo", "Balahna", "Balashiha", "Balashov", "Barnaul", "Batajsk", "Belgorod", "Belebej", "Belovo",
                "Belogorsk", "Beloreck", "Belorechensk", "Berdsk", "Berezniki",
                "Berezovskij", "Bijsk", "Birobidzhan", "Blagoveshchensk",
                "Bor", "Borisoglebsk", "Borovichi", "Bratsk", "Bryansk", "Bugul'ma", "Budennovsk", "Buzuluk", "Bujnaksk",
                "Velikie Luki", "Ve[likij Novgorod", "Verhnyaya Pyshma", "Vidnoe", "Vladivostok", "Vladikavkaz", "Vladimir",
                "Volgograd", "Volgodonsk", "Volzhsk", "Volzhskij", "Vologda", "Vol'sk", "Vorkuta", "Voronezh", "Voskresensk",
                "Votkinsk", "Vsevolozhsk", "Vyborg", "Vyksa", "Vyaz'ma", "Gatchina", "Gelendzhik"
        };
        return cities[random.nextInt(cities.length)];
    }

    public boolean getIsInDormitory(Random random) {
        return random.nextBoolean();
    }

    public short getWarningCount(Random random) {
        return (short) random.nextInt(5);
    }

    @Override
    protected List<Person> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Creating Person");
        Random random = new Random();
        Boolean isMale = random.nextInt(100) <= maleFemaleRatio;
        String role = getRole(random);

        return List.of(new Person(
                null,
                getFirstName(random, isMale),
                getSurname(random, isMale),
                getPatronymic(random, isMale),
                role,
                getBirthDate(random, role),
                getBirthPlace(random),
                getPersonNumber(),
                getIsInDormitory(random),
                getWarningCount(random)
        ));
    }
}
