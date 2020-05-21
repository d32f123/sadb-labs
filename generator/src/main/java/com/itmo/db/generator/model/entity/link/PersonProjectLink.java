package com.itmo.db.generator.model.entity.link;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class PersonProjectLink implements AbstractEntity<PersonProjectLink.PersonProjectLinkPK> {

    public PersonProjectLink(Integer person_id, Integer project_id, Timestamp participationStart, Timestamp endDate){
        this.id = new PersonProjectLinkPK(person_id, project_id);
        this.startDate = participationStart;
        this.endDate = endDate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonProjectLinkPK {
        public Integer personId;
        public Integer projectId;
    }

    private PersonProjectLinkPK id;
    private Timestamp startDate;
    private Timestamp endDate;
}
