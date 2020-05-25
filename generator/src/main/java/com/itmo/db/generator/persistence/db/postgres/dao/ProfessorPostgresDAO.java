package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "professors")
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorPostgresDAO implements IdentifiableDAO<Integer> {

    @Id
    @Column(name = "person_id")
    private Integer id;

    @Column(name = "faculty_id")
    private Integer faculty;

}
