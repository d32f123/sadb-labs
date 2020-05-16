package com.itmo.db.generator.model.entity;

public interface NumericallyIdentifiableEntity extends AbstractEntity<Integer> {

    void setId(Integer id);
    Integer getId();

}
