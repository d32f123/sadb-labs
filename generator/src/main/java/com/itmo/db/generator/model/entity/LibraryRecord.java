package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryRecord implements NumericallyIdentifiableEntity {

    private Integer id;
    private Integer personId;
    private Integer bookId;
    private String action;
    private Date actionDate;

}
