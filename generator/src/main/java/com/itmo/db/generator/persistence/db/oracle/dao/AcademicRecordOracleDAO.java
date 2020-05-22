package com.itmo.db.generator.persistence.db.oracle.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "academic_records")
@NoArgsConstructor
@AllArgsConstructor
public class AcademicRecordOracleDAO implements IdentifiableDAO<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "academic_record_id")
    private Long id;

    @Column(name = "degree", nullable = false)
    private String name;

    @Column(name = "budget", nullable = false)
    private boolean budget;

    @Column(name = "full_time", nullable = false)
    private boolean fullTime;

    @Column(name = "direction", nullable = false)
    private String direction;

    @Column(name = "speciality", nullable = false)
    private String speciality;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "subdivision", nullable = false)
    private String subdivision;

    @Column(name = "academic_year", nullable = false)
    private Date academicYear;

    @Column(name = "work_start_date", nullable = false)
    private Date startDate;

    @Column(name = "work_end_date", nullable = false)
    private Date endDate;
}
