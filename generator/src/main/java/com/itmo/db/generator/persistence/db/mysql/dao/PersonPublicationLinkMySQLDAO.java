package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "person_publications")
@IdClass(PersonPublicationLinkMySQLDAO.PersonPublicationLinkPK.class)
@AllArgsConstructor
@NoArgsConstructor
public class PersonPublicationLinkMySQLDAO implements IdentifiableDAO<PersonPublicationLinkMySQLDAO.PersonPublicationLinkPK> {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonPublicationLinkPK implements Serializable {
        private Long personId;
        private Long publicationId;
    }

    @Id
    @Column(name = "person_id")
    private Long personId;

    @Id
    @Column(name = "publication_id")
    private Long publicationId;

    public PersonPublicationLinkPK getId() {
        return new PersonPublicationLinkPK(
                this.personId,
                this.publicationId
        );
    }

    public void setId(PersonPublicationLinkPK id) {
        throw new UnsupportedOperationException("SetId not implemented");
    }

}
