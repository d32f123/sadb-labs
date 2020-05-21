package com.itmo.db.generator.model.entity.link;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class PersonGroupLink implements AbstractEntity<PersonGroupLink.PersonGroupLinkPK> {

    public PersonGroupLink(Integer person_id, Integer group_id){
        this.id = new PersonGroupLinkPK(person_id, group_id);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonGroupLinkPK {
        public Integer personId;
        public Integer groupId;
    }

    private PersonGroupLinkPK id;
}