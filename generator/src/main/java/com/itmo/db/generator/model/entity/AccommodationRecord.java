package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.AccommodationRecordMergeRepository;
import com.itmo.db.generator.persistence.db.mongo.dao.AccommodationRecordMongoDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityJpaRepository(clazz = AccommodationRecordMergeRepository.class)
@DAO(clazzes = AccommodationRecordMongoDAO.class)
public class AccommodationRecord implements NumericallyIdentifiableEntity {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer personId;
    private Integer roomId;

    private Boolean facilities;
    private Boolean budget;
    private Double payment;
    private LocalDate livingStartDate;
    private LocalDate livingEndDate;
    private String course;

}
