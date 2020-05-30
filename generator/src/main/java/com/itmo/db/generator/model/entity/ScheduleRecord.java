package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ItmoEntity(description = "Schedule record")
public class ScheduleRecord implements NumericallyIdentifiableEntity, OracleEntity {
    private Integer id;
    @ItmoAttribute
    @ItmoReference(Person.class)
    private Integer personId;
    @ItmoAttribute
    @ItmoReference(Discipline.class)
    private Integer disciplineId;
    @ItmoAttribute
    @ItmoReference(Semester.class)
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
