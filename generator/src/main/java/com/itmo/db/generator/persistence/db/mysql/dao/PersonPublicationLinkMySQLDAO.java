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
        private Long person;
        private Long publication;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private PersonMySQLDAO person;

    @Id
    @ManyToOne
    @JoinColumn(name = "publication_id", referencedColumnName = "publication_id")
    private PublicationMySQLDAO publication;

    public PersonPublicationLinkPK getId() {
        return new PersonPublicationLinkPK(
                this.person != null ? this.person.getId() : null,
                this.publication != null ? this.publication.getId() : null
        );
    }

    public void setId(PersonPublicationLinkPK id) {
        throw new UnsupportedOperationException("SetId not implemented");
    }

}
