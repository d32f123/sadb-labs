package com.itmo.db.generator.model.entity.link;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
public class PersonPublicationLink implements AbstractEntity<PersonPublicationLink.PersonPublicationLinkPK> {

    public PersonPublicationLink(Integer person_id, Integer publication_id) {
        this.id = new PersonPublicationLinkPK(person_id, publication_id);
    }

    // XCD
    @EmbeddedId
    private PersonPublicationLinkPK id;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonPublicationLinkPK implements Serializable {
        public Integer personId;
        public Integer publicationId;
    }
}
