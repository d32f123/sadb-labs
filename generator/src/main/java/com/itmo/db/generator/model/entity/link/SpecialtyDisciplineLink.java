package com.itmo.db.generator.model.entity.link;

import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.SpecialtyDisciplineLinkMergeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@EntityJpaRepository(clazz = SpecialtyDisciplineLinkMergeRepository.class)
public class SpecialtyDisciplineLink implements AbstractEntity<SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK> {

    public SpecialtyDisciplineLink(Integer specialty_id, Integer discipline_id) {
        this.id = new SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK(specialty_id, discipline_id);
    }

    @EmbeddedId
    private SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK id;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpecialtyDisciplineLinkPK implements Serializable { // lol
        public Integer specialtyId;
        public Integer disciplineId;
    }

    @Override
    public int getMergeKey() {
        return Objects.hash(id.specialtyId, id.disciplineId);
    }
}
