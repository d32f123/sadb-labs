package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conference implements NumericallyIdentifiableEntity {

    private Integer id;
    private String name;
    private String location;
    private Timestamp date;

}
