package com.itmo.db.generator.generator.entity;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.impl.*;
import com.itmo.db.generator.generator.entity.impl.link.*;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityGeneratorFactory {

    @Autowired
    private Generator generator;

    public EntityGeneratorFactory() {
    }

    public <T extends AbstractEntity<TId>, TId> EntityGenerator getGenerator(
            EntityDefinition<T, TId> entity
    ) {
        if (entity.getEntityClass().equals(AcademicRecord.class)) {
            return new AcademicRecordGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(AccommodationRecord.class)) {
            return new AccommodationRecordGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Conference.class)) {
            return new ConferenceGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Discipline.class)) {
            return new DisciplineGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Dormitory.class)) {
            return new DormitoryGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Faculty.class)) {
            return new FacultyGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Group.class)) {
            return new GroupGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Issue.class)) {
            return new IssueGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(LibraryRecordGenerator.class)) {
            return new LibraryRecordGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Person.class)) {
            return new PersonGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Professor.class)) {
            return new ProfessorGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Project.class)) {
            return new ProjectGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Publication.class)) {
            return new PublicationGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Room.class)) {
            return new RoomGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(ScheduleRecord.class)) {
            return new ScheduleRecordGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Semester.class)) {
            return new SemesterGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Specialty.class)) {
            return new SpecialtyGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(Student.class)) {
            return new StudentGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(StudentSemesterDiscipline.class)) {
            return new StudentSemesterDisciplineGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(University.class)) {
            return new UniversityGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(LibraryRecord.class)) {
            return new LibraryRecordGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(ConferencePublicationLink.class)) {
            return new ConferencePublicationLinkGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(IssuePublicationLink.class)) {
            return new IssuePublicationLinkGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(PersonGroupLink.class)) {
            return new PersonGroupLinkGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(PersonPublicationLink.class)) {
            return new PersonPublicationLinkGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(PersonProjectLink.class)) {
            return new PersonProjectLinkGenerator((EntityDefinition) entity, generator);
        }
        if (entity.getEntityClass().equals(SpecialtyDisciplineLink.class)) {
            return new SpecialtyDisciplineLinkGenerator((EntityDefinition) entity, generator);
        }

        throw new NullPointerException();
    }

}
