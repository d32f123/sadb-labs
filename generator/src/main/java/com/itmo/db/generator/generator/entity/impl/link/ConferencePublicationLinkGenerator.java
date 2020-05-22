package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.link.ConferencePublicationLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class ConferencePublicationLinkGenerator extends AbstractEntityGenerator<ConferencePublicationLink, ConferencePublicationLink.ConferencePublicationLinkPK> {

    public ConferencePublicationLinkGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(ConferencePublicationLink.class, deps, generator);
    }

    @Override
    protected List<ConferencePublicationLink> getEntities() {
        log.debug("Generating ConferencePublicationLink");
        return null;
    }
}
