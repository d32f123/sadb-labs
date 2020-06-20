package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.postgres.repository.FacultyPostgresRepository;
import com.itmo.db.generator.persistence.db.postgres.repository.PersonPostgresRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "persons")
@NoArgsConstructor
@AllArgsConstructor
@EntityJpaRepository(clazz = PersonPostgresRepository.class)
public class PersonPostgresDAO implements IdentifiableDAO<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Integer id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "patronymic_name")
    private String patronymicName;

    @Column(name = "role")
    private String role;

    @Column(name = "person_number")
    private String personNumber;

}
