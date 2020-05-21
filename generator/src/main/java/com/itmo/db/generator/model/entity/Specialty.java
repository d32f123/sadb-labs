package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Specialty implements NumericallyIdentifiableEntity {
    private Integer id;
    private Integer facultyId;
    private String name;
    private String studyStandard;
}
