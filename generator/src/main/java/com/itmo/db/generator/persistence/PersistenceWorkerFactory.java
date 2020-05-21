package com.itmo.db.generator.persistence;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.PersonProjectLink;
import com.itmo.db.generator.persistence.db.mysql.repository.*;
import com.itmo.db.generator.persistence.db.postgres.repository.*;
import com.itmo.db.generator.persistence.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersistenceWorkerFactory {

    @Autowired
    private Generator generator;

    @Autowired
    private ConferenceMySQLRepository conferenceMySQLRepository;

    @Autowired
    private ConferencePublicationLinkMySQLRepository conferencePublicationLinkMySQLRepository;

    @Autowired
    private IssueMySQLRepository issueMySQLRepository;

    @Autowired
    private IssuePublicationLinkMySQLRepository issuePublicationLinkMySQLRepository;

    @Autowired
    private PersonMySQLRepository personMySQLRepository;

    @Autowired
    private PersonProjectLinkMySQLRepository personProjectRepository;

    @Autowired
    private ProjectMySQLRepository projectMySQLRepository;

    @Autowired
    private PublicationMySQLRepository publicationMySQLRepository;

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

    public PersistenceWorkerFactory() {
    }

    public <T extends AbstractEntity<TId>, TId> PersistenceWorker getWorker(Class<T> entityClass) {
        if (entityClass.equals(Person.class)) {
            return new PersonPersistenceWorker(generator, personMySQLRepository);
        } else if (entityClass.equals(Project.class)) {
            return new ProjectPersistenceWorker(generator, projectMySQLRepository);
        } else if (entityClass.equals(PersonProjectLink.class)) {
            return new PersonProjectLinkPersistenceWorker(generator, personProjectRepository);
        } else if (entityClass.equals(Faculty.class)) {
            return new FacultyPersistenceWorker(generator, facultyPostgresRepository);
        } else if (entityClass.equals(Issue.class)) {
            return new IssuePersistenceWorker(generator, issueMySQLRepository);
        } else if (entityClass.equals(Conference.class)) {
            return new ConferencePersistenceWorker(generator, conferenceMySQLRepository);
        } else if (entityClass.equals(Publication.class)) {
            return new PublicationPersistenceWorker(generator, publicationMySQLRepository);
        } else if (entityClass.equals(Discipline.class)) {
            return new DisciplinePersistenceWorker(generator, disciplinePostgresRepository);
        } else if (entityClass.equals(University.class)) {
            return new UniversityPersistenceWorker(generator, universityPostgresRepository);
        }

        throw new NullPointerException();
    }

}
