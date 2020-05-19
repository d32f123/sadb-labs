package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Publication;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Set;

@Slf4j
public class PublicationGenerator extends AbstractEntityGenerator<Publication, Integer> {
    public PublicationGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Publication.class, deps, generator);
    }

    @Override
    protected Publication getEntity() {
        log.debug("Creating Publication");

        return new Publication(null, "name", "RU", 777, Timestamp.valueOf("2020-02-02 00:00:00"));
    }
}
