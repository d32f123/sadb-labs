package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.model.entity.link.PersonPublicationLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PersonPublicationLinkGenerator extends AbstractEntityGenerator<PersonPublicationLink, PersonPublicationLink.PersonPublicationLinkPK> {

    public PersonPublicationLinkGenerator(EntityDefinition<PersonPublicationLink, PersonPublicationLink.PersonPublicationLinkPK> entity, Generator generator) {
        super(entity, generator);
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
