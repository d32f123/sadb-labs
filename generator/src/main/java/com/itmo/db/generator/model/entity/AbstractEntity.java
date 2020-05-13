package com.itmo.db.generator.model.entity;

import java.io.Serializable;

public interface AbstractEntity<TId> extends Serializable {

    void setId(TId id);
    TId getId();

}
