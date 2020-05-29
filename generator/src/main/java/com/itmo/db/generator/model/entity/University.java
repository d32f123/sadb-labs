package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class University implements NumericallyIdentifiableEntity {

    private Integer id;
    private String name;
    private LocalDate creationDate;

}
