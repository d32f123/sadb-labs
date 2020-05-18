package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Issue;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class IssueGenerator extends AbstractEntityGenerator<Issue, Integer> {

    public IssueGenerator(Set<DependencyDefinition<?, ?>> dependencies, Generator generator) {
        super(Issue.class, dependencies, generator);
    }

    @Override
    protected Issue getEntity() {
        log.debug("Creating Issue");

        return new Issue(null, "name", "ru", "location", 255, "format");
    }
}
