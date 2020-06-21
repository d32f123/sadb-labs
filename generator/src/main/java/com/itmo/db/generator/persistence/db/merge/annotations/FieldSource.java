package com.itmo.db.generator.persistence.db.merge.annotations;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldSource {
    Class<? extends IdentifiableDAO> source();
}
