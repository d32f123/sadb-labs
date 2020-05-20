package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.PersonProjectLink;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
public class PersonProjectLinkGenerator extends AbstractEntityGenerator<PersonProjectLink, PersonProjectLink.PersonProjectLinkPK> {

    Timestamp getDate(Random random) {
        String y = "201" + random.nextInt(10);

        Integer i = 1 + random.nextInt(12);
        String m = (i > 9) ? String.valueOf(i) : ("0" + i);

        i = 1 + random.nextInt(28);
        String d = (i > 9) ? String.valueOf(i) : ("0" + i);

        return Timestamp.valueOf(y + "-" + m + "-" + d + " 03:00:00");
    }

    public PersonProjectLinkGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(PersonProjectLink.class, deps, generator);
    }

    @Override
    protected PersonProjectLink getEntity() {
        log.debug("Generating PersonProjectLink");
        Random random = new Random();

        Person person = this.getDependencyInstances(Person.class).get(0);
        Project project = this.getDependencyInstances(Project.class).get(0);

        return new PersonProjectLink(
                person.getId().longValue(), project.getId().longValue(), getDate(random), getDate(random)
        );
    }
}
