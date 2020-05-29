package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room implements NumericallyIdentifiableEntity {

    private Integer id;
    private int roomNumber;
    private short capacity;
    private short engaged;
    private boolean bugs;
    private LocalDate lastCleaningDate;
    private Integer dormitoryId;

}
