package com.itmo.db.generator.model.entity.link;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonPublicationLink implements AbstractEntity<PersonPublicationLink.PersonPublicationLinkPK> {

    public PersonPublicationLink(Integer person_id, Integer publication_id){
        this.id = new PersonPublicationLinkPK(person_id, publication_id);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonPublicationLinkPK {
        public Integer personId;
        public Integer publicationId;
    }

    private PersonPublicationLinkPK id;
}
