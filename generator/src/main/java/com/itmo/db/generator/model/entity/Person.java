package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ItmoEntity(description = "Basic person entity")
public class Person implements NumericallyIdentifiableEntity, OracleEntity {

    private Integer id;
    @ItmoAttribute
    private String firstName;
    @ItmoAttribute
    private String lastName;
    @ItmoAttribute
    private String patronymicName;
    private String role;
    @ItmoAttribute
    private LocalDate birthDate;
    @ItmoAttribute
    private String birthPlace;
    @ItmoAttribute
    private String personNumber;
    private boolean isInDormitory;
    private short warningCount;

    private LocalDate dateOfAppearance;

    public String getName() {
        return String.join(" ", this.lastName, this.firstName, this.patronymicName);
    }

}
