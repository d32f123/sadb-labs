package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "faculties")
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDAO implements IdentifiableDAO<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faculty_id")
    private Integer id;

    @Column(name = "faculty_name", nullable = false, unique = true)
    private String facultyName;


}