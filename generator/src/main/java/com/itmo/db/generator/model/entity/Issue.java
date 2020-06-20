package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.IssueMergeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityJpaRepository(clazz = IssueMergeRepository.class)
public class Issue implements NumericallyIdentifiableEntity {

    @Id
    private Integer id;
    private String name;
    private String language;
    private String location;
    private Integer length;
    private String format;

}
