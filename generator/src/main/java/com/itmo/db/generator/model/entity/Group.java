package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.oracle.annotations.Description;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Description(value = "Group of students")
public class Group implements NumericallyIdentifiableEntity, OracleEntity {

    private Integer id;
    @ItmoAttribute
    private String name;
    @ItmoAttribute
    private String course;
    @ItmoAttribute
    private Date startDate;
    @ItmoAttribute
    private Date endDate;
}
