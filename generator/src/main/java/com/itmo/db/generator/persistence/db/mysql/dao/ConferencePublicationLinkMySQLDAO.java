package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.annotations.FieldSource;
import com.itmo.db.generator.persistence.db.mysql.repository.ConferencePublicationLinkMySQLRepository;
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
@EntityJpaRepository(clazz = ConferencePublicationLinkMySQLRepository.class)
public class ConferencePublicationLinkMySQLDAO implements IdentifiableDAO<ConferencePublicationLinkMySQLDAO.ConferencePublicationLinkPK> {

    @Id
    @FieldSource(source = ConferenceMySQLDAO.class)
    @Column(name = "conference_id")
    private Long conferenceId;
    @Id
    @FieldSource(source = PublicationMySQLDAO.class)
    @Column(name = "publication_id")
    private Long publicationId;

    public ConferencePublicationLinkPK getId() {
        return new ConferencePublicationLinkPK(
                this.conferenceId, this.publicationId
        );
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConferencePublicationLinkPK implements Serializable {
        private Long conferenceId;
        private Long publicationId;
    }

    public void setId(ConferencePublicationLinkPK id) {
        throw new UnsupportedOperationException("SetId not implemented");
    }

}
