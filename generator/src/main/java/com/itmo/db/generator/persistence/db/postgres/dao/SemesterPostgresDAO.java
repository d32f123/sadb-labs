package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "semesters")
@NoArgsConstructor
@AllArgsConstructor
public class SemesterPostgresDAO implements IdentifiableDAO<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Integer id;

}
