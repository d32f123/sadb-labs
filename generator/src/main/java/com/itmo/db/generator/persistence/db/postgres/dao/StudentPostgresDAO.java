package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.postgres.repository.SpecialtyPostgresRepository;
import com.itmo.db.generator.persistence.db.postgres.repository.StudentPostgresRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@EntityJpaRepository(clazz = StudentPostgresRepository.class)
public class StudentPostgresDAO implements IdentifiableDAO<Integer> {

    @Id
    @Column(name = "person_id")
    private Integer id;

    @Column(name = "specialty_id")
    private Integer specialty;

    @Column(name = "study_type")
    private String studyType;

}
