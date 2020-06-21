package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements NumericallyIdentifiableEntity {

    private Integer id; // personId
    private Integer specialtyId;
    private String studyType;
    private String personNumber;

}
