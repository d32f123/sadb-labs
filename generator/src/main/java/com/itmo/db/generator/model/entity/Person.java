package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements NumericallyIdentifiableEntity {

    private Integer id;
    private String firstName;
    private String lastName;
    private String patronymicName;
    private String role;
    private Date birthDate;
    private String birthPlace;
    private String position;
    private String subdivision;
    private Date workStartDate;
    private Date workEndDate;

}
