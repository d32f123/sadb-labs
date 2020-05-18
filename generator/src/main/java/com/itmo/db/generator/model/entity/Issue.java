package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Issue implements NumericallyIdentifiableEntity {

    private Integer id;
    private String name;
    private String language;
    private String location;
    private Integer length;
    private String format;

}
