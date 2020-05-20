package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discipline implements NumericallyIdentifiableEntity {

    private Integer id;
    private String name;
    private String controlForm;
    private Integer lectureHours;
    private Integer practiceHours;
    private Integer labHours;

}
