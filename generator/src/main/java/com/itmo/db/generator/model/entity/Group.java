package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group implements NumericallyIdentifiableEntity {

    private Integer id;
    private String name;
    private String course;
    private Timestamp startDate;
    private Timestamp endDate;

}
