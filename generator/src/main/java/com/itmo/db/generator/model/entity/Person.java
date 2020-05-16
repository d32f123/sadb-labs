package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements NumericallyIdentifiableEntity {

    private Integer id;
    private String firstName;
    private String lastName;
    private String patronymicName;
    private String role;

}
