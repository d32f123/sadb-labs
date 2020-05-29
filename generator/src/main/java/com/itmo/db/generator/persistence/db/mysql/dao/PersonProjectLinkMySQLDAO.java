package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "person_projects")
@IdClass(PersonProjectLinkMySQLDAO.PersonProjectLinkPK.class)
@AllArgsConstructor
@NoArgsConstructor
public class PersonProjectLinkMySQLDAO implements IdentifiableDAO<PersonProjectLinkMySQLDAO.PersonProjectLinkPK> {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonProjectLinkPK implements Serializable {
        private Long personId;
        private Long projectId;
    }

    @Id
    @Column(name = "person_id")
    private Long personId;

    @Id
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "participation_start")
    private Timestamp participationStart;

    @Column(name = "participation_end")
    private Timestamp participationEnd;

    public PersonProjectLinkPK getId() {
        return new PersonProjectLinkPK(
                this.personId,
                this.projectId
        );
    }

    public void setId(PersonProjectLinkPK id) {
        throw new UnsupportedOperationException("SetId not implemented");
    }

}
