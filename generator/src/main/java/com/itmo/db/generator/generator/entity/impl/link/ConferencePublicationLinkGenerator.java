package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Conference;
import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.model.entity.University;
import com.itmo.db.generator.model.entity.link.ConferencePublicationLink;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class ConferencePublicationLinkGenerator extends AbstractEntityGenerator<ConferencePublicationLink, ConferencePublicationLink.ConferencePublicationLinkPK> {


    public ConferencePublicationLinkGenerator(EntityDefinition<ConferencePublicationLink, ConferencePublicationLink.ConferencePublicationLinkPK> entity, Generator generator) {
        super(entity, generator);
    }

    private ConferencePublicationLink getEntity(Conference conference, Publication publication) {
        return new ConferencePublicationLink(conference.getId(), publication.getId());
    }

    @Override
    protected List<ConferencePublicationLink> getEntities() {
        log.debug("Generating ConferencePublicationLink");

        return this.getDependencyInstances(Conference.class).stream().map(
                conference -> this.getDependencyInstances(Publication.class).stream().map(
                        publication -> getEntity(conference, publication)
                )
        ).reduce(Stream::concat).orElseThrow().collect(Collectors.toList());

    }
}
