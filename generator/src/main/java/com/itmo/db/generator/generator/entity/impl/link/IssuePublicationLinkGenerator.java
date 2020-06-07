package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Issue;
import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.model.entity.link.IssuePublicationLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class IssuePublicationLinkGenerator extends AbstractEntityGenerator<IssuePublicationLink, IssuePublicationLink.IssuePublicationLinkPK> {

    public IssuePublicationLinkGenerator(EntityDefinition<IssuePublicationLink, IssuePublicationLink.IssuePublicationLinkPK> entity, Generator generator) {
        super(entity, generator);
    }

    @Override
    protected List<IssuePublicationLink> getEntities() {
        log.debug("Generating IssuePublicationLink");

        Issue issue = this.getDependencyInstances(Issue.class).get(0);
        Publication publication = this.getDependencyInstances(Publication.class).get(0);

        return List.of(new IssuePublicationLink(
                issue.getId(), publication.getId()
        ));
    }
}
