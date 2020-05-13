package com.itmo.db.generator.model.entity.link;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PersonProjectLink implements AbstractEntity<PersonProjectLink.PersonProjectLinkPK> {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonProjectLinkPK {
        private Long personId;
        private Long projectId;
    }

    private PersonProjectLinkPK id;

}
