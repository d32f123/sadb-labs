package com.itmo.db.generator;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.PersonGroupLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Set<EntityDefinition<?, ?>> entities = Set.of(
                new EntityDefinition<>(AcademicRecord.class, 2, Set.of(
                        new DependencyDefinition<>(Person.class, 1)
                )),
                new EntityDefinition<>(AccommodationRecord.class, 2, Set.of(
                        new DependencyDefinition<>(Person.class, 1),
                        new DependencyDefinition<>(Room.class, 1)
                )),
//                new EntityDefinition<>(Conference.class, 2, null),
//                new EntityDefinition<>(ConferencePublicationLink.class, 2, Set.of(
//                        new DependencyDefinition<>(Conference.class, 1),
//                        new DependencyDefinition<>(Publication.class, 1)
//                )),
//                new EntityDefinition<>(Discipline.class, 2, null),
//                new EntityDefinition<>(Dormitory.class, 2, null),
                new EntityDefinition<>(Faculty.class, 2, Set.of(
                        new DependencyDefinition<>(University.class, 1)
                )),
                new EntityDefinition<>(Group.class, 2, null),
//                new EntityDefinition<>(Issue.class, 2, null),
//                new EntityDefinition<>(IssuePublicationLink.class, 2, Set.of(
//                        new DependencyDefinition<>(Issue.class, 1),
//                        new DependencyDefinition<>(Publication.class, 1)
//                )),
//                new EntityDefinition<>(LibraryRecord.class, 2, Set.of(
//                        new DependencyDefinition<>(Person.class, 1)
//                )),
                new EntityDefinition<>(Person.class, 2, null),
                new EntityDefinition<>(PersonGroupLink.class, 2, Set.of(
                        new DependencyDefinition<>(Person.class, 1),
                        new DependencyDefinition<>(Group.class, 1)
                )),
//                new EntityDefinition<>(PersonProjectLink.class, 2, Set.of(
//                        new DependencyDefinition<>(Person.class, 1),
//                        new DependencyDefinition<>(Project.class, 1)
//                )),
//                new EntityDefinition<>(PersonPublicationLink.class, 2, Set.of(
//                        new DependencyDefinition<>(Person.class, 1),
//                        new DependencyDefinition<>(Publication.class, 1)
//                )),
                new EntityDefinition<>(Professor.class, 2, Set.of(
                        new DependencyDefinition<>(Person.class, 1),
                        new DependencyDefinition<>(Faculty.class, 1)
                )),
//                new EntityDefinition<>(Project.class, 2, null),
//                new EntityDefinition<>(Publication.class, 2, null),
//                new EntityDefinition<>(Room.class, 2, Set.of(
//                        new DependencyDefinition<>(Dormitory.class, 1)
//                )),
//                new EntityDefinition<>(ScheduleRecord.class, 2, Set.of(
//                        new DependencyDefinition<>(StudentSemesterDiscipline.class, 1)
//                )),
//                new EntityDefinition<>(Semester.class, 2, null),
//                new EntityDefinition<>(Specialty.class, 2, Set.of(
//                        new DependencyDefinition<>(Faculty.class, 1)
//                )),
//                new EntityDefinition<>(SpecialtyDisciplineLink.class, 2, Set.of(
//                        new DependencyDefinition<>(Specialty.class, 1),
//                        new DependencyDefinition<>(Discipline.class, 1)
//                )),
//                new EntityDefinition<>(Student.class, 2, Set.of(
//                        new DependencyDefinition<>(Person.class, 1),
//                        new DependencyDefinition<>(Specialty.class, 1)
//                )),
//                new EntityDefinition<>(StudentSemesterDiscipline.class, 2, Set.of(
//                        new DependencyDefinition<>(Student.class, 1),
//                        new DependencyDefinition<>(Semester.class, 1),
//                        new DependencyDefinition<>(Discipline.class, 1),
//                        new DependencyDefinition<>(Professor.class, 1)
//                )),
                new EntityDefinition<>(University.class, 2, null)
        );
        this.generator.generate(entities);
    }
}
