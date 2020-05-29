package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationRecord implements NumericallyIdentifiableEntity {

    private Integer id;
    private Integer personId;
    private Integer roomId;

    private boolean facilities;
    private boolean budget;
    private double payment;
    private Date livingStartDate;
    private Date livingEndDate;
    private String course;

}
