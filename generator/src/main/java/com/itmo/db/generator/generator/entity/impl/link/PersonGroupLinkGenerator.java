package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Group;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.link.PersonGroupLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class PersonGroupLinkGenerator extends AbstractEntityGenerator<PersonGroupLink, PersonGroupLink.PersonGroupLinkPK> {

    public PersonGroupLinkGenerator(EntityDefinition<PersonGroupLink, PersonGroupLink.PersonGroupLinkPK> entity, Generator generator) {
        super(entity, generator);
    }

    private PersonGroupLink getEntity(Group group, Person person) {
        return new PersonGroupLink(person.getId(), group.getId());
    }

    @Override
    protected List<PersonGroupLink> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Generating PersonGroupLink");

        return this.getDependencyInstances(Group.class).stream().map(
                group -> this.getDependencyInstances(Person.class).stream().map(
                        person -> getEntity(group, person)
                )
        ).reduce(Stream::concat).orElseThrow().collect(Collectors.toList());

    }
}
