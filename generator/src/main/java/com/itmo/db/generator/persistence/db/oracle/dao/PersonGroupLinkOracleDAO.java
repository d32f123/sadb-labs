package com.itmo.db.generator.persistence.db.oracle.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "persons_groups")
@IdClass(PersonGroupLinkOracleDAO.PersonGroupLinkPK.class)
@AllArgsConstructor
@NoArgsConstructor
public class PersonGroupLinkOracleDAO implements IdentifiableDAO<PersonGroupLinkOracleDAO.PersonGroupLinkPK> {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonGroupLinkPK implements Serializable {
        private Long person;
        private Long group;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private PersonOracleDAO person;

    @Id
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private GroupOracleDAO group;


    public PersonGroupLinkPK getId() {
        return new PersonGroupLinkPK(
                this.person != null ? this.person.getId() : null,
                this.group != null ? this.group.getId() : null
        );
    }

    public void setId(PersonGroupLinkPK id) {
        throw new UnsupportedOperationException("SetId not implemented");
    }

}
