package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class PersonPublicationLinkGenerator extends AbstractEntityGenerator<PersonPublicationLink, PersonPublicationLink.PersonPublicationLinkPK> {

    public PersonPublicationLinkGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(PersonPublicationLink.class, deps, generator);
    }

    @Override
    protected List<PersonPublicationLink> getEntities() {
        log.debug("Generating PersonPublicationLink");

        Person person = this.getDependencyInstances(Person.class).get(0);
        Publication publication = this.getDependencyInstances(Publication.class).get(0);

        return List.of(new PersonPublicationLink(
                person.getId(), publication.getId()
        ));
    }
}
