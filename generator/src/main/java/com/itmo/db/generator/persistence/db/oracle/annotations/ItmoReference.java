package com.itmo.db.generator.persistence.db.oracle.annotations;

import com.itmo.db.generator.model.entity.NumericallyIdentifiableEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ItmoReference {
    Class<? extends NumericallyIdentifiableEntity> value();
}
