package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dormitory implements NumericallyIdentifiableEntity {

    private Integer id;
    private String address;
    private int roomCount;

}
