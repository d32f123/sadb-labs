package com.itmo.db.generator.model.entity.link;

import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.ConferencePublicationLinkMergeRepository;
import com.itmo.db.generator.persistence.db.mysql.dao.ConferencePublicationLinkMySQLDAO;
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
@EntityJpaRepository(clazz = ConferencePublicationLinkMergeRepository.class)
@DAO(clazzes = ConferencePublicationLinkMySQLDAO.class)
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
