package com.itmo.db.generator.persistence.db.merge.annotations;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DAO {
    Class<? extends IdentifiableDAO>[] clazzes();
}
