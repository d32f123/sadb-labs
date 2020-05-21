package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "professors")
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorPostgresDAO implements IdentifiableDAO<Integer> {

    @Id
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private PersonPostgresDAO person;

    @ManyToOne
    @JoinColumn(name = "faculty_id", referencedColumnName = "faculty_id")
    private FacultyPostgresDAO faculty;

}
