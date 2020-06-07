package com.itmo.db.generator.generator.entity.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.AbstractEntityGenerator;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.ScheduleRecord;
import com.itmo.db.generator.model.entity.StudentSemesterDiscipline;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Slf4j
public class ScheduleRecordGenerator extends AbstractEntityGenerator<ScheduleRecord, Integer> {

    private static final List<ScheduleEntry> scheduleEntries = List.of(
            ScheduleEntry.builder()
                    .startTime(LocalTime.of(8, 20))
                    .endTime(LocalTime.of(9, 50))
                    .build(),
            ScheduleEntry.builder()
                    .startTime(LocalTime.of(10, 0))
                    .endTime(LocalTime.of(11, 30))
                    .build(),
            ScheduleEntry.builder()
                    .startTime(LocalTime.of(11, 40))
                    .endTime(LocalTime.of(13, 10))
                    .build(),
            ScheduleEntry.builder()
                    .startTime(LocalTime.of(13, 30))
                    .endTime(LocalTime.of(15, 0))
                    .build(),
            ScheduleEntry.builder()
                    .startTime(LocalTime.of(15, 20))
                    .endTime(LocalTime.of(16, 50))
                    .build(),
            ScheduleEntry.builder()
                    .startTime(LocalTime.of(17, 0))
                    .endTime(LocalTime.of(18, 30))
                    .build(),
            ScheduleEntry.builder()
                    .startTime(LocalTime.of(18, 40))
                    .endTime(LocalTime.of(20, 10))
                    .build(),
            ScheduleEntry.builder()
                    .startTime(LocalTime.of(20, 20))
                    .endTime(LocalTime.of(21, 50))
                    .build()
    );

    public ScheduleRecordGenerator(EntityDefinition<ScheduleRecord, Integer> entity, Generator generator) {
        super(entity, generator);
    }

    private ScheduleEntry getEntry(Random random) {
        return scheduleEntries.get(random.nextInt(scheduleEntries.size()));
    }

    private String getClassroom(Random random) {
        return String.valueOf(random.nextInt(400) + 100);
    }

    @Override
    protected List<ScheduleRecord> getEntities() {
        log.debug("Creating ScheduleRecord");
        Random random = new Random();
        var studentSemesterDiscipline = this.getDependencyInstances(StudentSemesterDiscipline.class).get(0);
        var entry = getEntry(random);
        var record = new ScheduleRecord(
                null,
                studentSemesterDiscipline.getId().getStudentId(),
                studentSemesterDiscipline.getId().getDisciplineId(),
                studentSemesterDiscipline.getId().getSemesterId(),
                entry.getStartTime(),
                entry.getEndTime(),
                getClassroom(random)
        );

        return List.of(record);
    }

    @Data
    @Builder
    private static class ScheduleEntry {
        private LocalTime startTime;
        private LocalTime endTime;
    }
}


