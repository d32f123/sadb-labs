package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.ConferenceMergeRepository;
import com.itmo.db.generator.persistence.db.mongo.repository.AccommodationRecordMongoRepository;
import com.itmo.db.generator.persistence.db.mysql.repository.ConferenceMySQLRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "conferences")
@NoArgsConstructor
@AllArgsConstructor
@EntityJpaRepository(clazz = ConferenceMySQLRepository.class)
public class ConferenceMySQLDAO implements IdentifiableDAO<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conference_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "date")
    private Timestamp date;

}
