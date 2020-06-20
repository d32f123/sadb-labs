package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.ScheduleRecordMergeRepository;
import com.itmo.db.generator.persistence.db.mongo.dao.RoomMongoDAO;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ItmoEntity(description = "Schedule record")
@Entity
@EntityJpaRepository(clazz = ScheduleRecordMergeRepository.class)
public class ScheduleRecord implements NumericallyIdentifiableEntity, OracleEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @ItmoAttribute
    @ItmoReference(Person.class)
    private Integer personId;
    @ItmoAttribute
    @ItmoReference(Discipline.class)
    private Integer disciplineId;
    @ItmoAttribute
    @ItmoReference(value = Semester.class, isTransient = true)
    private Integer semesterId;
    @ItmoAttribute
    private LocalTime startTime;
    @ItmoAttribute
    private LocalTime endTime;
    @ItmoAttribute
    private String classroom;

    @Override
    public String getName() {
        return this.toString();
    }
}
