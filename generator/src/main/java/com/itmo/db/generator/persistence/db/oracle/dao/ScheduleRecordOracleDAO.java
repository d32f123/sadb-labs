package com.itmo.db.generator.persistence.db.oracle.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "schedules")
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRecordOracleDAO implements IdentifiableDAO<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private PersonOracleDAO person_id;

    @ManyToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "discipline_id")
    private PersonDisciplineDAO discipline_id;

    @ManyToOne
    @JoinColumn(name = "semester_id", referencedColumnName = "semester_id")
    private PersonsDisciplinesOracleDAO semester_id;

    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Column(name = "end_time", nullable = false)
    private Date endTime;

    @Column(name = "classroom", nullable = false)
    private String classroom;
}
