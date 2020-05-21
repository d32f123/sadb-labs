package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicRecord implements NumericallyIdentifiableEntity {

    private Integer id;
    private Integer personId;
    private Date academicYear;
    private String degree;
    private boolean budget;
    private boolean fullTime;
    private String direction;
    private String specialty;
    private String position;
    private String subdivision;
    private Date startDate;
    private Date endDate;

}
