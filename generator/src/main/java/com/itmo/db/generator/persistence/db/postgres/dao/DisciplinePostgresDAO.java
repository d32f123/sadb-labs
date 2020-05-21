package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "disciplines")
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinePostgresDAO implements IdentifiableDAO<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discipline_id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "control_form", nullable = false)
    private String controlForm;

    @Column(name = "lecture_hours")
    private Integer lectureHours;

    @Column(name = "practice_hours")
    private Integer practiceHours;

    @Column(name = "lab_hours")
    private Integer labHours;

}
