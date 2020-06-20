package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.SpecialtyMergeRepository;
import com.itmo.db.generator.persistence.db.postgres.dao.SemesterPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.SpecialtyPostgresDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityJpaRepository(clazz = SpecialtyMergeRepository.class)
@DAO(clazzes = SpecialtyPostgresDAO.class)
public class Specialty implements NumericallyIdentifiableEntity {

    @Id
    private Integer id;
    private Integer facultyId;
    private String name;
    private String studyStandard;
}
