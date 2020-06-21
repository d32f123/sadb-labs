package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.GroupMergeRepository;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ItmoEntity(description = "Group of students")
@Entity
@EntityJpaRepository(clazz = GroupMergeRepository.class)
@Table(name = "groups")
public class Group implements NumericallyIdentifiableEntity, OracleEntity {

    @Id
    @GeneratedValue
    private Integer id;
    @ItmoAttribute
    private String name;
    @ItmoAttribute
    private String course;
    @ItmoAttribute
    private LocalDate startDate;
    @ItmoAttribute
    private LocalDate endDate;

    @Override
    public int getMergeKey() {
        return Objects.hash(name, startDate, endDate);
    }
}
