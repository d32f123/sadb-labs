package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ItmoEntity(description = "Schedule record")
public class ScheduleRecord implements NumericallyIdentifiableEntity {
    private Integer id;
    private Integer personId;
    private Integer disciplineId;
    private Integer semesterId;
    @ItmoAttribute
    private Date startTime;
    @ItmoAttribute
    private Date endTime;
    @ItmoAttribute
    private String classroom;

}
