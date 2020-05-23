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
    private String bookId;
    private Action action;
    private Date actionDate;

    public enum Action {
        RETURNED,
        BORROWED
    }

}
