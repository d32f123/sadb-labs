package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.annotations.MergeKey;
import com.itmo.db.generator.persistence.db.merge.repository.PersonMergeRepository;
import com.itmo.db.generator.persistence.db.mongo.dao.PersonMongoDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.PersonMySQLDAO;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import com.itmo.db.generator.persistence.db.postgres.dao.PersonPostgresDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ItmoEntity(description = "Basic person entity")
@Entity
@EntityJpaRepository(clazz = PersonMergeRepository.class)
@DAO(clazzes = {PersonMySQLDAO.class, PersonPostgresDAO.class, PersonMongoDAO.class})
public class Person implements NumericallyIdentifiableEntity, OracleEntity {

    @Id
    @GeneratedValue
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
    @MergeKey
    private String personNumber;
    private Boolean isInDormitory;
    private Short warningCount;

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
    public int hashCode() {
        return Objects.hash(personNumber);
    }

    public Boolean isIsInDormitory(){
        return isInDormitory;
    }

    public void setIsInDormitory(Boolean isInDormitory){
        this.isInDormitory = isInDormitory;
    }

    public Boolean isInDormitory() {
        return isInDormitory;
    }
}
