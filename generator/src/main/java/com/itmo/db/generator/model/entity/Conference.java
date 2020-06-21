package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.ConferenceMergeRepository;
import com.itmo.db.generator.persistence.db.mongo.dao.AccommodationRecordMongoDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.ConferenceMySQLDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityJpaRepository(clazz = ConferenceMergeRepository.class)
@DAO(clazzes = ConferenceMySQLDAO.class)
public class Conference implements NumericallyIdentifiableEntity {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String location;
    @Column(name="conferenceDate")
    private Timestamp date;

    @Override
    public int getMergeKey() {
        return Objects.hash(name, location, date);
    }
}
