package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.postgres.repository.DisciplinePostgresRepository;
import com.itmo.db.generator.persistence.db.postgres.repository.FacultyPostgresRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "faculties")
@NoArgsConstructor
@AllArgsConstructor
@EntityJpaRepository(clazz = FacultyPostgresRepository.class)
public class FacultyPostgresDAO implements IdentifiableDAO<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faculty_id")
    private Integer id;

    @Column(name = "faculty_name")
    private String facultyName;

    @Column(name = "university_id")
    private Integer universityId;

}
