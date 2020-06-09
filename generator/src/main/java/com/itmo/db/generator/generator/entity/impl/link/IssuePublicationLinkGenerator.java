package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Issue;
import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.model.entity.link.IssuePublicationLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class IssuePublicationLinkGenerator extends AbstractEntityGenerator<IssuePublicationLink, IssuePublicationLink.IssuePublicationLinkPK> {

    public IssuePublicationLinkGenerator(EntityDefinition<IssuePublicationLink, IssuePublicationLink.IssuePublicationLinkPK> entity, Generator generator) {
        super(entity, generator);
    }

    private IssuePublicationLink getEntity(Issue conference, Publication publication) {
        return new IssuePublicationLink(conference.getId(), publication.getId());
    }

    @Override
    protected List<IssuePublicationLink> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Generating IssuePublicationLink");

        return this.getDependencyInstances(Issue.class).stream().map(
                issue -> this.getDependencyInstances(Publication.class).stream().map(
                        publication -> getEntity(issue, publication)
                )
        ).reduce(Stream::concat).orElseThrow().collect(Collectors.toList());

    }
}
