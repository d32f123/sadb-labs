package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.AcademicRecordMergeRepository;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ItmoEntity(description = "Record with additional information about student or university worker")
@Entity
@EntityJpaRepository(clazz = AcademicRecordMergeRepository.class)
public class AcademicRecord implements NumericallyIdentifiableEntity, OracleEntity {

    @Id
    @GeneratedValue
    private Integer id;
    @ItmoAttribute
    @ItmoReference(Person.class)
    private Integer personId;
    @ItmoAttribute
    private LocalDate academicYear;
    @ItmoAttribute
    private String degree;
    @ItmoAttribute
    private Boolean budget;
    @ItmoAttribute
    private Boolean fullTime;
    @ItmoAttribute
    private String direction;
    @ItmoAttribute
    private String specialty;
    @ItmoAttribute
    private String position;
    @ItmoAttribute
    private String subdivision;
    @ItmoAttribute
    private LocalDate startDate;
    @ItmoAttribute
    private LocalDate endDate;

    public String getName() {
        return this.toString();
    }

}
