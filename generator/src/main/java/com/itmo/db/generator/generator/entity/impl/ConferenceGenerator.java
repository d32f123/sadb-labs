package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.Conference;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Set;

@Slf4j
public class ConferenceGenerator extends AbstractEntityGenerator<Conference, Integer> {
    public ConferenceGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(Conference.class, deps, generator);
    }

    @Override
    protected Conference getEntity() {
        log.debug("Creating Conference");

        return new Conference(null, "name", "location", Timestamp.valueOf("2020-02-02 00:00:00"));
    }
}
