package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.link.SpecialtyDisciplineLink;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.util.List;

public class SpecialtyDisciplineLinkPersistenceWorker
        extends AbstractPersistenceWorker<SpecialtyDisciplineLink, SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK> {

    public SpecialtyDisciplineLinkPersistenceWorker(Generator generator) {
        super(SpecialtyDisciplineLink.class, generator);
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(SpecialtyDisciplineLink entity) {
        return null;
    }

    @Override
    protected void doCommit() {
    }
}
