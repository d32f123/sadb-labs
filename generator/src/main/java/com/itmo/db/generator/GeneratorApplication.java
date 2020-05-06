package com.itmo.db.generator;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.model.EntityWithAmount;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Project;
import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.model.link.AbstractLink;
import com.itmo.db.generator.model.link.PersonProjectLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<EntityWithAmount> entities = Stream.of(
                Person.class,
                Project.class,
                Publication.class
        )
                .map(cls -> EntityWithAmount.builder().entityClass(cls).amount(10).build())
                .collect(Collectors.toList());
        List<Class<? extends AbstractLink>> links = Collections.singletonList(PersonProjectLink.class);

        this.generator.generate(entities, links);
    }
}
