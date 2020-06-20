package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.ProjectMergeRepository;
import com.itmo.db.generator.persistence.db.mysql.dao.ProjectMySQLDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.ProfessorPostgresDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityJpaRepository(clazz = ProjectMergeRepository.class)
@DAO(clazzes = ProjectMySQLDAO.class)
public class Project implements NumericallyIdentifiableEntity {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;

}
