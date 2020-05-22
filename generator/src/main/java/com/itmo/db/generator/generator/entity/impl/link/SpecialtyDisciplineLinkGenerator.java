package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.link.SpecialtyDisciplineLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class SpecialtyDisciplineLinkGenerator extends AbstractEntityGenerator<SpecialtyDisciplineLink, SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK> {

    public SpecialtyDisciplineLinkGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(SpecialtyDisciplineLink.class, deps, generator);
    }

    @Override
    protected List<SpecialtyDisciplineLink> getEntities() {
        log.debug("Generating SpecialtyDisciplineLink");
        return null;
    }
}
