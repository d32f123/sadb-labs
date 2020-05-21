package com.itmo.db.generator.model.entity.link;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConferencePublicationLink implements AbstractEntity<ConferencePublicationLink.ConferencePublicationLinkPK> {

    public ConferencePublicationLink(Integer conferenceId, Integer publicationId){
        this.id = new ConferencePublicationLinkPK(conferenceId, publicationId);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConferencePublicationLinkPK {
        public Integer conferenceId;
        public Integer publicationId;
    }

    private ConferencePublicationLinkPK id;
}
