package com.itmo.db.generator;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Faculty;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Professor;
import com.itmo.db.generator.model.entity.University;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
public class GeneratorApplication implements ApplicationRunner {

    protected final Generator generator;

    public GeneratorApplication(@Autowired Generator generator) {
        this.generator = generator;
    }

    public static void main(String[] args) {
        SpringApplication.run(GeneratorApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Set<EntityDefinition<?, ?>> entities = Set.of(
//                new EntityDefinition<>(Group.class, 2, null),
                new EntityDefinition<>(Person.class, 2, null),
//                new EntityDefinition<>(Project.class, 2, null),
                new EntityDefinition<>(University.class, 2, null),
                new EntityDefinition<>(Faculty.class, 2, Set.of(
                        new DependencyDefinition<>(University.class, 1)
                )),
//                new EntityDefinition<>(PersonProjectLink.class, 2, Set.of(
//                        new DependencyDefinition<>(Person.class, 1),
//                        new DependencyDefinition<>(Project.class, 1)
//                )),
//                new EntityDefinition<>(PersonGroupLink.class, 2, Set.of(
//                        new DependencyDefinition<>(Person.class, 1),
//                        new DependencyDefinition<>(Group.class, 1)
//                )),
//                new EntityDefinition<>(AcademicRecord.class, 2, null),
                new EntityDefinition<>(Professor.class, 2, Set.of(
                        new DependencyDefinition<>(Person.class, 1),
                        new DependencyDefinition<>(Faculty.class, 1)
                ))
        );
        this.generator.generate(entities);
    }
}
