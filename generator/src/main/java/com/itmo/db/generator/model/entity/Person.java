package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ItmoEntity(description = "Basic person entity")
public class Person implements NumericallyIdentifiableEntity {

    private Integer id;
    @ItmoAttribute
    private String firstName;
    @ItmoAttribute
    private String lastName;
    @ItmoAttribute
    private String patronymicName;
    private String role;
    @ItmoAttribute
    private Date birthDate;
    @ItmoAttribute
    private String birthPlace;
    private boolean isInDormitory;
    private short warningCount;

}
