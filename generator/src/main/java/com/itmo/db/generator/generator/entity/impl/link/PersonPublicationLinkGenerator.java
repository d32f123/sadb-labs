package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Group;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.model.entity.link.PersonGroupLink;
import com.itmo.db.generator.model.entity.link.PersonPublicationLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class PersonPublicationLinkGenerator extends AbstractEntityGenerator<PersonPublicationLink, PersonPublicationLink.PersonPublicationLinkPK> {

    public PersonPublicationLinkGenerator(EntityDefinition<PersonPublicationLink, PersonPublicationLink.PersonPublicationLinkPK> entity, Generator generator) {
        super(entity, generator);
    }

    private PersonPublicationLink getEntity(Person person, Publication publication) {
        return new PersonPublicationLink(person.getId(), publication.getId());
    }

    @Override
    protected List<PersonPublicationLink> getEntities() {
        log.debug("Generating PersonPublicationLink");

        return this.getDependencyInstances(Person.class).stream().map(
                person -> this.getDependencyInstances(Publication.class).stream().map(
                        publication -> getEntity(person, publication)
                )
        ).reduce(Stream::concat).orElseThrow().collect(Collectors.toList());

    }
}
