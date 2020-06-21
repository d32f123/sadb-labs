package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Professor implements AbstractEntity<Integer> {
    private Integer id; // personId
    private Integer facultyId;
    private String personNumber;
}
