package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.RoomMergeRepository;
import com.itmo.db.generator.persistence.db.mongo.dao.RoomMongoDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PublicationMySQLDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityJpaRepository(clazz = RoomMergeRepository.class)
@DAO(clazzes = RoomMongoDAO.class)
public class Room implements NumericallyIdentifiableEntity {

    @Id
    private Integer id;
    private int roomNumber;
    private short capacity;
    private short engaged;
    private boolean bugs;
    private LocalDate lastCleaningDate;
    private Integer dormitoryId;

}
