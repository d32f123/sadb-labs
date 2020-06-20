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
public class ConferencePublicationLink implements AbstractEntity<ConferencePublicationLink.ConferencePublicationLinkPK> {

    public ConferencePublicationLink(Integer conferenceId, Integer publicationId) {
        this.id = new ConferencePublicationLinkPK(conferenceId, publicationId);
    }

    // ?????
    @EmbeddedId
    private ConferencePublicationLinkPK id;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class ConferencePublicationLinkPK implements Serializable {
        public Integer conferenceId;
        public Integer publicationId;
    }
}
