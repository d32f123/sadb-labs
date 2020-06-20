package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.postgres.repository.SpecialtyDisciplineLinkPostgresRepository;
import com.itmo.db.generator.persistence.db.postgres.repository.SpecialtyPostgresRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "specialties")
@NoArgsConstructor
@AllArgsConstructor
@EntityJpaRepository(clazz = SpecialtyPostgresRepository.class)
public class SpecialtyPostgresDAO implements IdentifiableDAO<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialty_id")
    private Integer id;

    @Column(name = "specialty_name")
    private String specialtyName;

    @Column(name = "study_standard")
    private String studyStandard;

    @Column(name = "faculty_id")
    private Integer facultyId;

}
