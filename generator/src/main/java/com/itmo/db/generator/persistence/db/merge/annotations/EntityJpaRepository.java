package com.itmo.db.generator.persistence.db.merge.annotations;

import org.springframework.data.repository.CrudRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityJpaRepository {
    Class<? extends CrudRepository> clazz();
}
