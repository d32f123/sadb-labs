package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.LibraryRecord;

import java.util.List;
import java.util.Set;

public class LibraryRecordGenerator extends AbstractEntityGenerator<LibraryRecord, Integer> {
    public LibraryRecordGenerator(Set<DependencyDefinition<?, ?>> dependencies, Generator generator) {
        super(LibraryRecord.class, dependencies, generator);
    }

    @Override
    protected List<LibraryRecord> getEntities() {
        return null;
    }
}
