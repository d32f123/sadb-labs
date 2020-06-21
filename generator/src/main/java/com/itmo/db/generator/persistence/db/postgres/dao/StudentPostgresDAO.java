package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.annotations.FieldSource;
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
    @FieldSource(source = PersonPostgresDAO.class)
    private Integer id;

    @Column(name = "specialty_id")
    @FieldSource(source = SpecialtyPostgresDAO.class)
    private Integer specialtyId;

    @Column(name = "study_type")
    private String studyType;

    @Column(name = "person_number")
    private String personNumber;

}
