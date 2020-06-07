package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Conference;
import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.model.entity.link.ConferencePublicationLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ConferencePublicationLinkGenerator extends AbstractEntityGenerator<ConferencePublicationLink, ConferencePublicationLink.ConferencePublicationLinkPK> {

    public ConferencePublicationLinkGenerator(EntityDefinition<ConferencePublicationLink, ConferencePublicationLink.ConferencePublicationLinkPK> entity, Generator generator) {
        super(entity, generator);
    }

    @Override
    protected List<ConferencePublicationLink> getEntities() {
        log.debug("Generating ConferencePublicationLink");

        Conference conference = this.getDependencyInstances(Conference.class).get(0);
        Publication publication = this.getDependencyInstances(Publication.class).get(0);

        return List.of(new ConferencePublicationLink(
                conference.getId(), publication.getId()
        ));
    }
}
