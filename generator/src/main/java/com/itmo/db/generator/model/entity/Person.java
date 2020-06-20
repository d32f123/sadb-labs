package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.annotations.MergeKey;
import com.itmo.db.generator.persistence.db.merge.repository.PersonMergeRepository;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ItmoEntity(description = "Basic person entity")
@Entity
@EntityJpaRepository(clazz = PersonMergeRepository.class)
public class Person implements NumericallyIdentifiableEntity, OracleEntity {

    @Id
    private Integer id;
    @ItmoAttribute
    private String firstName;
    @ItmoAttribute
    private String lastName;
    @ItmoAttribute
    private String patronymicName;
    private String role;
    @ItmoAttribute
    private LocalDate birthDate;
    @ItmoAttribute
    private String birthPlace;
    @ItmoAttribute
    private String personNumber;
    private boolean isInDormitory;
    private short warningCount;

    public String getName() {
        return String.join(" ", this.lastName, this.firstName, this.patronymicName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return personNumber.equals(person.personNumber);
    }

    @Override
    @MergeKey
    public int hashCode() {
        return Objects.hash(personNumber);
    }
}
