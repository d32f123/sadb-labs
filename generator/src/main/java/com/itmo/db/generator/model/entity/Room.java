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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityJpaRepository(clazz = RoomMergeRepository.class)
@DAO(clazzes = RoomMongoDAO.class)
public class Room implements NumericallyIdentifiableEntity {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer roomNumber;
    private Short capacity;
    private Short engaged;
    private Boolean bugs;
    private LocalDate lastCleaningDate;
    private Integer dormitoryId;

    @Override
    public int getMergeKey() {
        return Objects.hash(roomNumber, capacity, engaged, bugs, lastCleaningDate, dormitoryId);
    }
}
