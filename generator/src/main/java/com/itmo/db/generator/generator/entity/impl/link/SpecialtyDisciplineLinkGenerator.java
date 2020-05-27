package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class SpecialtyDisciplineLinkGenerator extends AbstractEntityGenerator<SpecialtyDisciplineLink, SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK> {

    public SpecialtyDisciplineLinkGenerator(Set<DependencyDefinition<?, ?>> deps, Generator generator) {
        super(SpecialtyDisciplineLink.class, deps, generator);
    }

    @Override
    protected List<SpecialtyDisciplineLink> getEntities() {
        log.debug("Generating SpecialtyDisciplineLink");

        Specialty specialty = this.getDependencyInstances(Specialty.class).get(0);
        Discipline discipline = this.getDependencyInstances(Discipline.class).get(0);

        return List.of(new SpecialtyDisciplineLink(
                specialty.getId(), discipline.getId()
        ));
    }
}
