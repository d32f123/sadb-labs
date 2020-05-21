package com.itmo.db.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRecord implements NumericallyIdentifiableEntity {
    private Integer id;

    private Integer personId;
    private Integer disciplineId;
    private Integer semesterId;
    private Date startTime;
    private Date endTime;
    private String classroom;

}
