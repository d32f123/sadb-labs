package com.itmo.db.generator.persistence;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.*;
import com.itmo.db.generator.persistence.db.mongo.repository.AccommodationRecordMongoRepository;
import com.itmo.db.generator.persistence.db.mongo.repository.DormitoryMongoRepository;
import com.itmo.db.generator.persistence.db.mongo.repository.PersonMongoRepository;
import com.itmo.db.generator.persistence.db.mongo.repository.RoomMongoRepository;
import com.itmo.db.generator.persistence.db.mysql.repository.*;
import com.itmo.db.generator.persistence.db.oracle.repository.*;
import com.itmo.db.generator.persistence.db.postgres.repository.*;
import com.itmo.db.generator.persistence.impl.*;
import com.itmo.db.generator.persistence.impl.itmo.ItmoEntityAbstractPersistenceWorker;
import com.itmo.db.generator.persistence.impl.itmo.ItmoGroupPersistenceWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersistenceWorkerFactory {

    private final Generator generator;

    //MySQL section START
    private final ConferenceMySQLRepository conferenceMySQLRepository;

    private final ConferencePublicationLinkMySQLRepository conferencePublicationLinkMySQLRepository;

    private final IssueMySQLRepository issueMySQLRepository;

    private final IssuePublicationLinkMySQLRepository issuePublicationLinkMySQLRepository;

    private final LibraryRecordMySQLRepository libraryRecordMySQLRepository;

    private final PersonMySQLRepository personMySQLRepository;

    private final PersonProjectLinkMySQLRepository personProjectRepository;

    private final PersonPublicationLinkMySQLRepository personPublicationLinkMySQLRepository;

    private final ProjectMySQLRepository projectMySQLRepository;

    private final PublicationMySQLRepository publicationMySQLRepository;
    //MySQL section END

    //Oracle section START
    private final ItmoObjectOracleRepository itmoObjectOracleRepository;

    private final ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository;

    private final ItmoAttributeOracleRepository itmoAttributeOracleRepository;

    private final ItmoParamOracleRepository itmoParamOracleRepository;

    private final ItmoListValueOracleRepository itmoListValueOracleRepository;
    //Oracle section END

    //Postgres section START
    private final DisciplinePostgresRepository disciplinePostgresRepository;

    private final FacultyPostgresRepository facultyPostgresRepository;

    private final PersonPostgresRepository personPostgresRepository;

    private final ProfessorPostgresRepository professorPostgresRepository;

    private final SemesterPostgresRepository semesterPostgresRepository;

    private final SpecialtyDisciplineLinkPostgresRepository specialtyDisciplineLinkPostgresRepository;

    private final SpecialtyPostgresRepository specialtyPostgresRepository;

    private final StudentPostgresRepository studentPostgresRepository;

    private final StudentSemesterDisciplinePostgresRepository studentSemesterDisciplinePostgresRepository;

    private final UniversityPostgresRepository universityPostgresRepository;
    //Postgres section END

    // MongoDB section START
    private final AccommodationRecordMongoRepository accommodationRecordMongoRepository;

    private final DormitoryMongoRepository dormitoryMongoRepository;

    private final PersonMongoRepository personMongoRepository;

    private final RoomMongoRepository roomMongoRepository;
    // MongoDB section END


    @Autowired
    public PersistenceWorkerFactory(ConferenceMySQLRepository conferenceMySQLRepository,
                                    Generator generator,
                                    ConferencePublicationLinkMySQLRepository conferencePublicationLinkMySQLRepository,
                                    ProfessorPostgresRepository professorPostgresRepository,
                                    PersonMongoRepository personMongoRepository,
                                    ItmoParamOracleRepository itmoParamOracleRepository,
                                    SpecialtyPostgresRepository specialtyPostgresRepository,
                                    DormitoryMongoRepository dormitoryMongoRepository,
                                    ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository,
                                    ItmoAttributeOracleRepository itmoAttributeOracleRepository,
                                    IssueMySQLRepository issueMySQLRepository,
                                    IssuePublicationLinkMySQLRepository issuePublicationLinkMySQLRepository,
                                    LibraryRecordMySQLRepository libraryRecordMySQLRepository,
                                    PersonPostgresRepository personPostgresRepository,
                                    StudentSemesterDisciplinePostgresRepository studentSemesterDisciplinePostgresRepository,
                                    PersonMySQLRepository personMySQLRepository,
                                    ItmoObjectOracleRepository itmoObjectOracleRepository,
                                    StudentPostgresRepository studentPostgresRepository,
                                    UniversityPostgresRepository universityPostgresRepository,
                                    SemesterPostgresRepository semesterPostgresRepository,
                                    AccommodationRecordMongoRepository accommodationRecordMongoRepository,
                                    DisciplinePostgresRepository disciplinePostgresRepository,
                                    PersonProjectLinkMySQLRepository personProjectRepository,
                                    PersonPublicationLinkMySQLRepository personPublicationLinkMySQLRepository,
                                    FacultyPostgresRepository facultyPostgresRepository,
                                    ProjectMySQLRepository projectMySQLRepository,
                                    ItmoListValueOracleRepository itmoListValueOracleRepository,
                                    SpecialtyDisciplineLinkPostgresRepository specialtyDisciplineLinkPostgresRepository,
                                    PublicationMySQLRepository publicationMySQLRepository,
                                    RoomMongoRepository roomMongoRepository) {
        this.conferenceMySQLRepository = conferenceMySQLRepository;
        this.generator = generator;
        this.conferencePublicationLinkMySQLRepository = conferencePublicationLinkMySQLRepository;
        this.professorPostgresRepository = professorPostgresRepository;
        this.personMongoRepository = personMongoRepository;
        this.itmoParamOracleRepository = itmoParamOracleRepository;
        this.specialtyPostgresRepository = specialtyPostgresRepository;
        this.dormitoryMongoRepository = dormitoryMongoRepository;
        this.itmoObjectTypeOracleRepository = itmoObjectTypeOracleRepository;
        this.itmoAttributeOracleRepository = itmoAttributeOracleRepository;
        this.issueMySQLRepository = issueMySQLRepository;
        this.issuePublicationLinkMySQLRepository = issuePublicationLinkMySQLRepository;
        this.libraryRecordMySQLRepository = libraryRecordMySQLRepository;
        this.personPostgresRepository = personPostgresRepository;
        this.studentSemesterDisciplinePostgresRepository = studentSemesterDisciplinePostgresRepository;
        this.personMySQLRepository = personMySQLRepository;
        this.itmoObjectOracleRepository = itmoObjectOracleRepository;
        this.studentPostgresRepository = studentPostgresRepository;
        this.universityPostgresRepository = universityPostgresRepository;
        this.semesterPostgresRepository = semesterPostgresRepository;
        this.accommodationRecordMongoRepository = accommodationRecordMongoRepository;
        this.disciplinePostgresRepository = disciplinePostgresRepository;
        this.personProjectRepository = personProjectRepository;
        this.personPublicationLinkMySQLRepository = personPublicationLinkMySQLRepository;
        this.facultyPostgresRepository = facultyPostgresRepository;
        this.projectMySQLRepository = projectMySQLRepository;
        this.itmoListValueOracleRepository = itmoListValueOracleRepository;
        this.specialtyDisciplineLinkPostgresRepository = specialtyDisciplineLinkPostgresRepository;
        this.publicationMySQLRepository = publicationMySQLRepository;
        this.roomMongoRepository = roomMongoRepository;
        ItmoEntityAbstractPersistenceWorker.init(
                itmoAttributeOracleRepository,
                itmoListValueOracleRepository,
                itmoObjectOracleRepository,
                itmoObjectTypeOracleRepository,
                itmoParamOracleRepository
        );
    }

    private <T extends AbstractEntity<TId>, TId> ItmoEntityAbstractPersistenceWorker<T, TId> getItmoEntityWorker(Class<T> entityClass) {
        return new ItmoEntityAbstractPersistenceWorker<>(entityClass, generator);
    }

    public <T extends AbstractEntity<TId>, TId> PersistenceWorker getWorker(Class<T> entityClass) {
        if (entityClass.equals(AcademicRecord.class)) {
            return new AcademicRecordPersistenceWorker(generator, getItmoEntityWorker(AcademicRecord.class));
        }
        if (entityClass.equals(AccommodationRecord.class)) {
            return new AccommodationRecordPersistenceWorker(generator, accommodationRecordMongoRepository);
        }
        if (entityClass.equals(Conference.class)) {
            return new ConferencePersistenceWorker(generator, conferenceMySQLRepository);
        }
        if (entityClass.equals(ConferencePublicationLink.class)) {
            return new ConferencePublicationLinkPersistenceWorker(generator, conferencePublicationLinkMySQLRepository);
        }
        if (entityClass.equals(Discipline.class)) {
            return new DisciplinePersistenceWorker(generator, disciplinePostgresRepository, getItmoEntityWorker(Discipline.class));
        }
        if (entityClass.equals(Dormitory.class)) {
            return new DormitoryPersistenceWorker(generator, dormitoryMongoRepository);
        }
        if (entityClass.equals(Faculty.class)) {
            return new FacultyPersistenceWorker(generator, facultyPostgresRepository);
        }
        if (entityClass.equals(Group.class)) {
            return new GroupPersistenceWorker(generator, new ItmoGroupPersistenceWorker(Group.class, generator));
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
            return new PersonGroupLinkPersistenceWorker(generator, getItmoEntityWorker(PersonGroupLink.class));
        }
        if (entityClass.equals(Person.class)) {
            return new PersonPersistenceWorker(generator,
                    personMySQLRepository,
                    personPostgresRepository,
                    getItmoEntityWorker(Person.class),
                    personMongoRepository);
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
        if (entityClass.equals(PersonPublicationLink.class)) {
            return new PersonPublicationLinkPersistenceWorker(generator, personPublicationLinkMySQLRepository);
        }
        if (entityClass.equals(Room.class)) {
            return new RoomPersistenceWorker(generator, roomMongoRepository);
        }
        if (entityClass.equals(ScheduleRecord.class)) {
            return new ScheduleRecordPersistenceWorker(generator, getItmoEntityWorker(ScheduleRecord.class));
        }
        if (entityClass.equals(Semester.class)) {
            return new SemesterPersistenceWorker(generator, semesterPostgresRepository);
        }
        if (entityClass.equals(SpecialtyDisciplineLink.class)) {
            return new SpecialtyDisciplineLinkPersistenceWorker(generator, specialtyDisciplineLinkPostgresRepository);
        }
        if (entityClass.equals(Specialty.class)) {
            return new SpecialtyPersistenceWorker(generator, specialtyPostgresRepository);
        }
        if (entityClass.equals(Student.class)) {
            return new StudentPersistenceWorker(generator, studentPostgresRepository);
        }
        if (entityClass.equals(StudentSemesterDiscipline.class)) {
            return new StudentSemesterDisciplinePersistenceWorker(generator,
                    studentSemesterDisciplinePostgresRepository,
                    getItmoEntityWorker(StudentSemesterDiscipline.class));
        }
        if (entityClass.equals(University.class)) {
            return new UniversityPersistenceWorker(generator, universityPostgresRepository);
        }

        throw new NullPointerException();
    }

}
