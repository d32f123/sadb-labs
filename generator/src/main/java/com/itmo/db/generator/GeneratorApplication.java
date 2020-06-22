package com.itmo.db.generator;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.impl.DisciplineGenerator;
import com.itmo.db.generator.generator.entity.impl.DormitoryGenerator;
import com.itmo.db.generator.generator.entity.impl.UniversityGenerator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.*;
import com.itmo.db.generator.persistence.db.mongo.repository.AccommodationRecordMongoRepository;
import com.itmo.db.generator.persistence.db.mongo.repository.RoomMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;
import java.util.Random;
import java.util.Set;

@SpringBootApplication
public class GeneratorApplication implements ApplicationRunner {

    protected final Generator generator;

    public GeneratorApplication(@Autowired Generator generator) {
        this.generator = generator;
    }

    public static void main(String[] args) {
        SpringApplication.run(GeneratorApplication.class, args);
    }

    @Autowired
    private RoomMongoRepository roomMongoRepository;
    @Autowired
    private AccommodationRecordMongoRepository accommodationRecordMongoRepository;

    private void shuffleRecords() {
        var random = new Random();
        var records = this.accommodationRecordMongoRepository.findAll();
        var rooms = roomMongoRepository.findAll();
        Collections.shuffle(rooms);
        int i = 0;
        for (var record : records) {
            record.setRoomId(rooms.get(random.nextInt(rooms.size())).getId());
        }
        this.accommodationRecordMongoRepository.saveAll(records);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.shuffleRecords();
        return;
//
//        int baseAmount = 500;
//
//        Set<EntityDefinition<?, ?>> entities = Set.of(
//                new EntityDefinition<>(AcademicRecord.class, null, Set.of(
//                        new DependencyDefinition<>(Person.class, 1)
//                )),
//                new EntityDefinition<>(AccommodationRecord.class, null, Set.of(
//                        new DependencyDefinition<>(Person.class, 20),
//                        new DependencyDefinition<>(Room.class, 1)
//                )),
//                new EntityDefinition<>(Conference.class, null, Set.of(
//                        new DependencyDefinition<>(Publication.class, 10)
//                )),
//                new EntityDefinition<>(ConferencePublicationLink.class, null, Set.of(
//                        new DependencyDefinition<>(Conference.class, 1)
//                )),
//                new EntityDefinition<>(Discipline.class, DisciplineGenerator.disciplines.size(), null),
//                new EntityDefinition<>(Dormitory.class, DormitoryGenerator.addresses.size(), null),
//                new EntityDefinition<>(Faculty.class, null, Set.of(
//                        new DependencyDefinition<>(University.class, 1)
//                )),
//                new EntityDefinition<>(Group.class, baseAmount, null),
//                new EntityDefinition<>(Issue.class, baseAmount, null),
//                new EntityDefinition<>(IssuePublicationLink.class, null, Set.of(
//                        new DependencyDefinition<>(Issue.class, 1),
//                        new DependencyDefinition<>(Publication.class, 3)
//                )),
//                new EntityDefinition<>(LibraryRecord.class, null, Set.of(
//                        new DependencyDefinition<>(Person.class, 1)
//                )),
//                new EntityDefinition<>(Person.class, baseAmount, null),
//                new EntityDefinition<>(PersonGroupLink.class, null, Set.of(
//                        new DependencyDefinition<>(Person.class, 15),
//                        new DependencyDefinition<>(Group.class, 1)
//                )),
//                new EntityDefinition<>(PersonProjectLink.class, null, Set.of(
//                        new DependencyDefinition<>(Person.class, 1),
//                        new DependencyDefinition<>(Project.class, 4)
//                )),
//                new EntityDefinition<>(PersonPublicationLink.class, null, Set.of(
//                        new DependencyDefinition<>(Publication.class, 1)
//                )),
//                new EntityDefinition<>(Professor.class, null, Set.of(
//                        new DependencyDefinition<>(Person.class, 1),
//                        new DependencyDefinition<>(Faculty.class, 1)
//                )),
//                new EntityDefinition<>(Project.class, baseAmount, null),
//                new EntityDefinition<>(Publication.class, null, Set.of(
//                        new DependencyDefinition<>(Person.class, 4)
//                )),
//                new EntityDefinition<>(Room.class, (baseAmount / 48), Set.of(
//                        new DependencyDefinition<>(Dormitory.class, 1)
//                )),
//                new EntityDefinition<>(ScheduleRecord.class, null, Set.of(
//                        new DependencyDefinition<>(StudentSemesterDiscipline.class, 1)
//                )),
//                new EntityDefinition<>(Semester.class, baseAmount, null),
//                new EntityDefinition<>(Specialty.class, null, Set.of(
//                        new DependencyDefinition<>(Faculty.class, 1)
//                )),
//                new EntityDefinition<>(SpecialtyDisciplineLink.class, null, Set.of(
//                        new DependencyDefinition<>(Specialty.class, 1),
//                        new DependencyDefinition<>(Discipline.class, 6)
//                )),
//                new EntityDefinition<>(Student.class, null, Set.of(
//                        new DependencyDefinition<>(Person.class, 100),
//                        new DependencyDefinition<>(Specialty.class, 1)
//                )),
//                new EntityDefinition<>(StudentSemesterDiscipline.class, null, Set.of(
//                        new DependencyDefinition<>(Student.class, 100),
//                        new DependencyDefinition<>(Semester.class, 2),
//                        new DependencyDefinition<>(Discipline.class, 6),
//                        new DependencyDefinition<>(Professor.class, 2)
//                )),
//                new EntityDefinition<>(University.class, UniversityGenerator.names.size(), null)
//        );
//        this.generator.generate(entities);
    }
}
