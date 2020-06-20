package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.PublicationMergeRepository;
import com.itmo.db.generator.persistence.db.mysql.dao.PublicationMySQLDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.ProfessorPostgresDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityJpaRepository(clazz = PublicationMergeRepository.class)
@DAO(clazzes = PublicationMySQLDAO.class)
public class Publication implements NumericallyIdentifiableEntity {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String language;
    private Integer citation_index;
    @Column(name="publicationDate")
    private Timestamp date;
}
