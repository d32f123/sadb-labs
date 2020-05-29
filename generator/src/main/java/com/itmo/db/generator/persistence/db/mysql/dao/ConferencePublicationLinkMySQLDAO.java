package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "conference_publications")
@IdClass(ConferencePublicationLinkMySQLDAO.ConferencePublicationLinkPK.class)
@AllArgsConstructor
@NoArgsConstructor
public class ConferencePublicationLinkMySQLDAO implements IdentifiableDAO<ConferencePublicationLinkMySQLDAO.ConferencePublicationLinkPK> {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConferencePublicationLinkPK implements Serializable {
        private Long conference;
        private Long publication;
    }

    @Id
    @Column(name = "conference_id")
    private Long conference_id;

    @Id
    @Column(name = "publication_id")
    private Long publication_id;

    public ConferencePublicationLinkPK getId() {
        return new ConferencePublicationLinkPK(
                this.conference_id, this.publication_id
        );
    }

    public void setId(ConferencePublicationLinkPK id) {
        throw new UnsupportedOperationException("SetId not implemented");
    }

}
