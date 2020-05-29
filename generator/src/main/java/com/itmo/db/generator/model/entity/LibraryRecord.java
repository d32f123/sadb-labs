package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryRecord implements NumericallyIdentifiableEntity {

    private Integer id;
    private Integer personId;
    private String bookId;
    private String action;
    private LocalDate actionDate;

}
