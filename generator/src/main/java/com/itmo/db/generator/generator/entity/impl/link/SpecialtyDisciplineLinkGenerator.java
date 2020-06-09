package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Discipline;
import com.itmo.db.generator.model.entity.Specialty;
import com.itmo.db.generator.model.entity.link.SpecialtyDisciplineLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SpecialtyDisciplineLinkGenerator extends AbstractEntityGenerator<SpecialtyDisciplineLink, SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK> {

    public SpecialtyDisciplineLinkGenerator(EntityDefinition<SpecialtyDisciplineLink, SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK> entity, Generator generator) {
        super(entity, generator);
    }

    private SpecialtyDisciplineLink getEntity(Specialty specialty, Discipline discipline) {
        return new SpecialtyDisciplineLink(specialty.getId(), discipline.getId());
    }

    @Override
    protected List<SpecialtyDisciplineLink> getEntities() {
        if (log.isDebugEnabled())
            log.debug("Generating SpecialtyDisciplineLink");

        return this.getDependencyInstances(Specialty.class).stream().map(
                specialty -> this.getDependencyInstances(Discipline.class).stream().map(
                        discipline -> getEntity(specialty, discipline)
                )
        ).reduce(Stream::concat).orElseThrow().collect(Collectors.toList());

    }
}
