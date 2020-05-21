package com.itmo.db.generator.persistence.db.oracle.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "persons")
@NoArgsConstructor
@AllArgsConstructor
public class GroupOracleDAO implements IdentifiableDAO<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "course", nullable = false)
    private String course;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;
}
