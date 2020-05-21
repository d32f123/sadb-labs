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
        private Long person;
        private Long project;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private PersonMySQLDAO person;

    @Id
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    private ProjectMySQLDAO project;

    @Column(name = "participation_start")
    private Timestamp participationStart;

    @Column(name = "participation_end")
    private Timestamp participationEnd;

    public PersonProjectLinkPK getId() {
        return new PersonProjectLinkPK(
                this.person != null ? this.person.getId() : null,
                this.project != null ? this.project.getId() : null
        );
    }

    public void setId(PersonProjectLinkPK id) {
        throw new UnsupportedOperationException("SetId not implemented");
    }

}
