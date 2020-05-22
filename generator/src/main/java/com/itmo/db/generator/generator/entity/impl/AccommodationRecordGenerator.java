package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.AccommodationRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class AccommodationRecordGenerator extends AbstractEntityGenerator<AccommodationRecord, Integer> {

    public AccommodationRecordGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(AccommodationRecord.class, deps, generator);
    }

    @Override
    protected List<AccommodationRecord> getEntities() {
        log.debug("Creating AccommodationRecord");
        return null;
    }
}

