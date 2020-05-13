package com.itmo.db.generator.persistence.db.mysql.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "person_projects")
@IdClass(PersonProjectLinkDAO.PersonProjectLinkPK.class)
@AllArgsConstructor
@NoArgsConstructor
public class PersonProjectLinkDAO {

    @Data
    public static class PersonProjectLinkPK implements Serializable {
        private Long person;
        private Long project;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private PersonDAO person;

    @Id
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    private ProjectDAO project;

    @Column(name = "participation_start")
    private Timestamp participationStart;

    @Column(name = "participation_end")
    private Timestamp participationEnd;

}
