package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.DormitoryMergeRepository;
import com.itmo.db.generator.persistence.db.mongo.dao.DormitoryMongoDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityJpaRepository(clazz = DormitoryMergeRepository.class)
@DAO(clazzes = DormitoryMongoDAO.class)
public class Dormitory implements NumericallyIdentifiableEntity {

    @Id
    private Integer id;
    private String address;
    private int roomCount;

}
