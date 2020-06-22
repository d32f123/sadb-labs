package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Conference;
import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.model.entity.link.ConferencePublicationLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ConferencePublicationLinkGenerator extends AbstractEntityGenerator<ConferencePublicationLink, ConferencePublicationLink.ConferencePublicationLinkPK> {


    public ConferencePublicationLinkGenerator(EntityDefinition<ConferencePublicationLink, ConferencePublicationLink.ConferencePublicationLinkPK> entity, Generator generator) {
        super(entity, generator);
    }

    @Override
    protected List<ConferencePublicationLink> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Generating ConferencePublicationLink");

        var conference = this.getDependencyInstances(Conference.class).get(0);
        return conference.getPublications().stream().map(pub -> new ConferencePublicationLink(
                conference.getId(),
                pub.getId()
        )).collect(Collectors.toUnmodifiableList());
    }
}
