package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.ScheduleRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class ScheduleRecordGenerator extends AbstractEntityGenerator<ScheduleRecord, Integer> {

    public ScheduleRecordGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(ScheduleRecord.class, deps, generator);
    }

    @Override
    protected List<ScheduleRecord> getEntities() {
        log.debug("Creating ScheduleRecord");
        return null;
    }
}


