package com.itmo.db.generator.persistence;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.*;
import com.itmo.db.generator.persistence.db.mysql.repository.*;
import com.itmo.db.generator.persistence.db.oracle.repository.*;
import com.itmo.db.generator.persistence.db.postgres.repository.*;
import com.itmo.db.generator.persistence.impl.*;
import com.itmo.db.generator.persistence.impl.itmo.ItmoGroupPersistenceWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersistenceWorkerFactory {

    @Autowired
    private Generator generator;

    //MySQL section START
    @Autowired
    private ConferenceMySQLRepository conferenceMySQLRepository;

    @Autowired
    private ConferencePublicationLinkMySQLRepository conferencePublicationLinkMySQLRepository;

    @Autowired
    private IssueMySQLRepository issueMySQLRepository;

    @Autowired
    private IssuePublicationLinkMySQLRepository issuePublicationLinkMySQLRepository;

    @Autowired
    private LibraryRecordMySQLRepository libraryRecordMySQLRepository;

    @Autowired
    private PersonMySQLRepository personMySQLRepository;

    @Autowired
    private PersonProjectLinkMySQLRepository personProjectRepository;

    @Autowired
    private ProjectMySQLRepository projectMySQLRepository;

    @Autowired
    private PublicationMySQLRepository publicationMySQLRepository;
    //MySQL section END

    //Oracle section START
    // TODO: Redo with ObjectRepository and ParamsRepository
    @Autowired
    private ItmoObjectOracleRepository itmoObjectOracleRepository;

    @Autowired
    private ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository;

    @Autowired
    private ItmoAttributeOracleRepository itmoAttributeOracleRepository;

    @Autowired
    private ItmoParamOracleRepository itmoParamOracleRepository;

    @Autowired
    private ItmoListValueOracleRepository itmoListValueOracleRepository;
    //Oracle section END

    //Postgres section START
    @Autowired
    private DisciplinePostgresRepository disciplinePostgresRepository;

    @Autowired
    private FacultyPostgresRepository facultyPostgresRepository;

    @Autowired
    private PersonPostgresRepository personPostgresRepository;

    @Autowired
    private ProfessorPostgresRepository professorPostgresRepository;

    @Autowired
    private SemesterPostgresRepository semesterPostgresRepository;

    @Autowired
    private SpecialtyDisciplineLinkPostgresRepository specialtyDisciplineLinkPostgresRepository;

    @Autowired
    private SpecialtyPostgresRepository specialtyPostgresRepository;

    @Autowired
    private StudentPostgresRepository studentPostgresRepository;

    @Autowired
    private StudentSemesterDisciplinePostgresRepository studentSemesterDisciplinePostgresRepository;

    @Autowired
    private UniversityPostgresRepository universityPostgresRepository;
    //Postgres section END


    public PersistenceWorkerFactory() {
    }

    public <T extends AbstractEntity<TId>, TId> PersistenceWorker getWorker(Class<T> entityClass) {
        if (entityClass.equals(AcademicRecord.class)) {
            return new AcademicRecordPersistenceWorker(generator);
        }
        if (entityClass.equals(AccommodationRecord.class)) {
            return new AccommodationRecordPersistenceWorker(generator);
        }
        if (entityClass.equals(Conference.class)) {
            return new ConferencePersistenceWorker(generator, conferenceMySQLRepository);
        }
        if (entityClass.equals(ConferencePublicationLink.class)) {
            return new ConferencePublicationLinkPersistenceWorker(generator, conferencePublicationLinkMySQLRepository);
        }
        if (entityClass.equals(Discipline.class)) {
            return new DisciplinePersistenceWorker(generator, disciplinePostgresRepository);
        }
        if (entityClass.equals(Dormitory.class)) {
            return new DormitoryPersistenceWorker(generator);
        }
        if (entityClass.equals(Faculty.class)) {
            return new FacultyPersistenceWorker(generator, facultyPostgresRepository);
        }
        if (entityClass.equals(Group.class)) {
            return new GroupPersistenceWorker(generator, new ItmoGroupPersistenceWorker(Group.class, generator, itmoAttributeOracleRepository, itmoListValueOracleRepository, itmoObjectOracleRepository, itmoObjectTypeOracleRepository, itmoParamOracleRepository));
        }
        if (entityClass.equals(Issue.class)) {
            return new IssuePersistenceWorker(generator, issueMySQLRepository);
        }
        if (entityClass.equals(IssuePublicationLink.class)) {
            return new IssuePublicationLinkPersistenceWorker(generator, issuePublicationLinkMySQLRepository);
        }
        if (entityClass.equals(LibraryRecord.class)) {
            return new LibraryRecordPersistenceWorker(generator, libraryRecordMySQLRepository);
        }
        if (entityClass.equals(PersonGroupLink.class)) {
            return new PersonGroupLinkPersistenceWorker(generator);
        }
        if (entityClass.equals(Person.class)) {
            return new PersonPersistenceWorker(generator, personMySQLRepository, personPostgresRepository);
        }
        if (entityClass.equals(PersonProjectLink.class)) {
            return new PersonProjectLinkPersistenceWorker(generator, personProjectRepository);
        }
        if (entityClass.equals(Professor.class)) {
            return new ProfessorPersistenceWorker(generator, professorPostgresRepository);
        }
        if (entityClass.equals(Project.class)) {
            return new ProjectPersistenceWorker(generator, projectMySQLRepository);
        }
        if (entityClass.equals(Publication.class)) {
            return new PublicationPersistenceWorker(generator, publicationMySQLRepository);
        }
        if (entityClass.equals(Room.class)) {
            return new RoomPersistenceWorker(generator);
        }
        if (entityClass.equals(ScheduleRecord.class)) {
            return new ScheduleRecordPersistenceWorker(generator);
        }
        if (entityClass.equals(Semester.class)) {
            return new SemesterPersistenceWorker(generator);
        }
        if (entityClass.equals(SpecialtyDisciplineLink.class)) {
            return new SpecialtyDisciplineLinkPersistenceWorker(generator);
        }
        if (entityClass.equals(Specialty.class)) {
            return new SpecialtyPersistenceWorker(generator);
        }
        if (entityClass.equals(Student.class)) {
            return new StudentPersistenceWorker(generator);
        }
        if (entityClass.equals(StudentSemesterDiscipline.class)) {
            return new StudentSemesterDisciplinePersistenceWorker(generator);
        }
        if (entityClass.equals(University.class)) {
            return new UniversityPersistenceWorker(generator, universityPostgresRepository);
        }

        throw new NullPointerException();
    }

}
