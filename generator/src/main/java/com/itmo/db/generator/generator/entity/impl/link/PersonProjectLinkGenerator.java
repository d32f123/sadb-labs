package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.link.PersonProjectLink;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class PersonProjectLinkGenerator extends AbstractEntityGenerator<PersonProjectLink, PersonProjectLink.PersonProjectLinkPK> {

    public PersonProjectLinkGenerator(
            Set<DependencyDefinition<?, ?>> dependencies,
            Generator generator) {
        super(PersonProjectLink.class, dependencies, generator);
    }

    @Override
    protected PersonProjectLink getEntity() {
        log.debug("Generating PersonProjectLink");
        PersonProjectLink personProjectLink = new PersonProjectLink();

        return personProjectLink;
    }
}
