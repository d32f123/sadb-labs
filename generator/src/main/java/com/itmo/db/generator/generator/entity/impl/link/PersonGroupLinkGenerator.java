package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Group;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.link.PersonGroupLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class PersonGroupLinkGenerator extends AbstractEntityGenerator<PersonGroupLink,PersonGroupLink.PersonGroupLinkPK> {

    public PersonGroupLinkGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(PersonGroupLink.class, deps, generator);
    }

    @Override
    protected List<PersonGroupLink> getEntities() {
        log.debug("Generating PersonGroupLink");

        Person person = this.getDependencyInstances(Person.class).get(0);
        Group group = this.getDependencyInstances(Group.class).get(0);

        return List.of(new PersonGroupLink(
                person.getId(), group.getId()
        ));
    }
}
