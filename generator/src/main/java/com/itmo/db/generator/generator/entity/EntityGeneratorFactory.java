package com.itmo.db.generator.generator.entity;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.generator.entity.impl.*;
import com.itmo.db.generator.generator.entity.impl.link.*;
import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EntityGeneratorFactory {

    @Autowired
    private Generator generator;

    public EntityGeneratorFactory() {}

    public <T extends AbstractEntity<TId>, TId> EntityGenerator getGenerator(Class<T> entityClass,
                                                                      Set<DependencyDefinition<?, ?>> deps) {
        if (entityClass.equals(AcademicRecord.class)) {
            return new AcademicRecordGenerator(deps, generator);
        }
        if (entityClass.equals(AccommodationRecord.class)) {
            return new AccommodationRecordGenerator(deps, generator);
        }
        if (entityClass.equals(Conference.class)) {
            return new ConferenceGenerator(deps, generator);
        }
        if (entityClass.equals(Discipline.class)) {
            return new DisciplineGenerator(deps, generator);
        }
        if (entityClass.equals(Dormitory.class)) {
            return new DormitoryGenerator(deps, generator);
        }
        if (entityClass.equals(Faculty.class)) {
            return new FacultyGenerator(deps, generator);
        }
        if (entityClass.equals(Group.class)) {
            return new GroupGenerator(deps, generator);
        }
        if (entityClass.equals(Issue.class)) {
            return new IssueGenerator(deps, generator);
        }
        if (entityClass.equals(Person.class)) {
            return new PersonGenerator(deps, generator);
        }
        if (entityClass.equals(Professor.class)) {
            return new ProfessorGenerator(deps, generator);
        }
        if (entityClass.equals(Project.class)) {
            return new ProjectGenerator(deps, generator);
        }
        if (entityClass.equals(Publication.class)) {
            return new PublicationGenerator(deps, generator);
        }
        if (entityClass.equals(Room.class)) {
            return new RoomGenerator(deps, generator);
        }
        if (entityClass.equals(ScheduleRecord.class)) {
            return new ScheduleRecordGenerator(deps, generator);
        }
        if (entityClass.equals(Semester.class)) {
            return new SemesterGenerator(deps, generator);
        }
        if (entityClass.equals(Specialty.class)) {
            return new SpecialtyGenerator(deps, generator);
        }
        if (entityClass.equals(Student.class)) {
            return new StudentGenerator(deps, generator);
        }
        if (entityClass.equals(StudentSemesterDiscipline.class)) {
            return new StudentSemesterDisciplineGenerator(deps, generator);
        }
        if (entityClass.equals(University.class)) {
            return new UniversityGenerator(deps, generator);
        }

        if (entityClass.equals(ConferencePublicationLink.class)) {
            return new ConferencePublicationLinkGenerator(deps, generator);
        }
        if (entityClass.equals(IssuePublicationLink.class)) {
            return new IssuePublicationLinkGenerator(deps, generator);
        }
        if (entityClass.equals(PersonGroupLink.class)) {
            return new PersonGroupLinkGenerator(deps, generator);
        }
        if (entityClass.equals(PersonProjectLink.class)) {
            return new PersonProjectLinkGenerator(deps, generator);
        }
        if (entityClass.equals(SpecialtyDisciplineLink.class)) {
            return new SpecialtyDisciplineLinkGenerator(deps, generator);
        }

        throw new NullPointerException();
    }

}
