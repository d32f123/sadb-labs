package com.itmo.db.generator.persistence.db;

public interface IdentifiableDAO<TId> {
    TId getId();
    void setId(TId id);
}
