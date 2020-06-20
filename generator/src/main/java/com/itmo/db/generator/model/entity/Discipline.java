package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.annotations.MergeKey;
import com.itmo.db.generator.persistence.db.merge.repository.DisciplineMergeRepository;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ItmoEntity(description = "University subject used to map other entities")
@Entity
@EntityJpaRepository(clazz = DisciplineMergeRepository.class)
public class Discipline implements NumericallyIdentifiableEntity, OracleEntity {

    @Id
    private Integer id;
    @ItmoAttribute
    private String name;
    private String controlForm;
    private Integer lectureHours;
    private Integer practiceHours;
    private Integer labHours;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discipline that = (Discipline) o;
        return name.equals(that.name);
    }

    @Override
    @MergeKey
    public int hashCode() {
        return Objects.hash(name);
    }
}
