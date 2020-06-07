package com.itmo.db.generator.generator.entity.impl.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.Discipline;
import com.itmo.db.generator.model.entity.Specialty;
import com.itmo.db.generator.model.entity.link.SpecialtyDisciplineLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SpecialtyDisciplineLinkGenerator extends AbstractEntityGenerator<SpecialtyDisciplineLink, SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK> {

    public SpecialtyDisciplineLinkGenerator(EntityDefinition<SpecialtyDisciplineLink, SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK> entity, Generator generator) {
        super(entity, generator);
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
