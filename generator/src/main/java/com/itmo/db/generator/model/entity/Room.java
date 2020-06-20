package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.RoomMergeRepository;
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
