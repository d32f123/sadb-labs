package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.annotations.FieldSource;
import com.itmo.db.generator.persistence.db.mysql.repository.PersonMySQLRepository;
import com.itmo.db.generator.persistence.db.mysql.repository.PersonProjectLinkMySQLRepository;
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
@EntityJpaRepository(clazz = PersonProjectLinkMySQLRepository.class)
public class PersonProjectLinkMySQLDAO implements IdentifiableDAO<PersonProjectLinkMySQLDAO.PersonProjectLinkPK> {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonProjectLinkPK implements Serializable {
        private Long personId;
        private Long projectId;
    }

    @Id
    @FieldSource(source = PersonMySQLDAO.class)
    @Column(name = "person_id")
    private Long personId;

    @Id
    @FieldSource(source = ProjectMySQLDAO.class)
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "participation_start")
    private Timestamp startDate;

    @Column(name = "participation_end")
    private Timestamp endDate;

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
