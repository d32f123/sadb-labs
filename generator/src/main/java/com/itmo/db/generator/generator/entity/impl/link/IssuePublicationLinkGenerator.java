package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.link.IssuePublicationLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class IssuePublicationLinkGenerator extends AbstractEntityGenerator<IssuePublicationLink, IssuePublicationLink.IssuePublicationLinkPK> {

    public IssuePublicationLinkGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(IssuePublicationLink.class, deps, generator);
    }

    @Override
    protected List<IssuePublicationLink> getEntities() {
        log.debug("Generating IssuePublicationLink");
        return null;
    }
}
