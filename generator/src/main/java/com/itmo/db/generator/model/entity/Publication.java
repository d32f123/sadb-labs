package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Publication implements NumericallyIdentifiableEntity {

    private Integer id;
    private String name;
    private String language;
    private Integer citation_index;
    private Timestamp date;

    private List<Person> authors;
}
