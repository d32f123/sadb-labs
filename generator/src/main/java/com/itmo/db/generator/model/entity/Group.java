package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group implements NumericallyIdentifiableEntity {

    private Integer id;
    private String name;
    private String course;
    private Date startDate;
    private Date endDate;
}
