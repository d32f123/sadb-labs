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
    @ManyToOne
    @JoinColumn(name = "conference_id", referencedColumnName = "conference_id")
    private ConferenceMySQLDAO conference;

    @Id
    @ManyToOne
    @JoinColumn(name = "publication_id", referencedColumnName = "publication_id")
    private PublicationMySQLDAO publication;

    public ConferencePublicationLinkPK getId() {
        return new ConferencePublicationLinkPK(
                this.conference != null ? this.conference.getId() : null,
                this.publication != null ? this.publication.getId() : null
        );
    }

    public void setId(ConferencePublicationLinkPK id) {
        throw new UnsupportedOperationException("SetId not implemented");
    }

}
