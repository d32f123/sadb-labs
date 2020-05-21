package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
public class StudentPostgresDAO implements IdentifiableDAO<Integer> {

    @Id
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private PersonPostgresDAO person;

    @ManyToOne
    @JoinColumn(name = "specialty_id", referencedColumnName = "specialty_id")
    private SpecialtyPostgresDAO specialty;

    @Column(name = "study_type")
    private String studyType;

}
