package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.StudentMergeRepository;
import com.itmo.db.generator.persistence.db.postgres.dao.SpecialtyPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.StudentPostgresDAO;
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
@EntityJpaRepository(clazz = StudentMergeRepository.class)
@DAO(clazzes = StudentPostgresDAO.class)
public class Student implements NumericallyIdentifiableEntity {

    @Id
    @GeneratedValue
    private Integer id; // personId
    private Integer specialtyId;
    private String studyType;

}
