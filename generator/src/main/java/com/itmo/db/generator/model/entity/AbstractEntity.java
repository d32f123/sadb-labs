package com.itmo.db.generator.model.entity;

import java.io.Serializable;
import java.util.Objects;

public interface AbstractEntity<TId> extends Serializable {

    void setId(TId id);
    TId getId();

    default int getMergeKey(){
        return getId().hashCode();
    };
}
