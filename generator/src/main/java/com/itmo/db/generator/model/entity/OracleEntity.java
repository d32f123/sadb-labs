package com.itmo.db.generator.model.entity;

public interface OracleEntity  {
    public static Integer getClassId() {
        //replace OracleEntity with needed class name
        return OracleEntity.class.getName().hashCode();
    }
}
