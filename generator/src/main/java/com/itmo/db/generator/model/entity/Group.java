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
@ItmoEntity(description = "Group of students")
public class Group implements NumericallyIdentifiableEntity, OracleEntity {

    private Integer id;
    @ItmoAttribute
    private String name;
    @ItmoAttribute
    private String course;
    @ItmoAttribute
    private LocalDate startDate;
    @ItmoAttribute
    private LocalDate endDate;
}
