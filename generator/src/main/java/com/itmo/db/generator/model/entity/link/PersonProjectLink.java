package com.itmo.db.generator.model.entity.link;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
public class PersonProjectLink implements AbstractEntity<PersonProjectLink.PersonProjectLinkPK> {

    public PersonProjectLink(Integer person_id, Integer project_id, Timestamp participationStart, Timestamp endDate) {
        this.id = new PersonProjectLinkPK(person_id, project_id);
        this.startDate = participationStart;
        this.endDate = endDate;
    }

    @EmbeddedId
    private PersonProjectLinkPK id;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonProjectLinkPK implements Serializable {
        public Integer personId;
        public Integer projectId;
    }

    private Timestamp startDate;
    private Timestamp endDate;
}
