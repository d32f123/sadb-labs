package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ItmoEntity(description = "University subject used to map other entities")
public class Discipline implements NumericallyIdentifiableEntity {

    private Integer id;
    @ItmoAttribute
    private String name;
    private String controlForm;
    private Integer lectureHours;
    private Integer practiceHours;
    private Integer labHours;

}
