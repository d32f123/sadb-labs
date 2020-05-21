package com.itmo.db.generator.model.entity.link;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpecialtyDisciplineLink implements AbstractEntity<SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK> {

    public SpecialtyDisciplineLink(Integer specialty_id, Integer discipline_id){
        this.id = new SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK(specialty_id, discipline_id);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpecialtyDisciplineLinkPK {
        public Integer specialtyId;
        public Integer disciplineId;
    }

    private SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK id;
}
