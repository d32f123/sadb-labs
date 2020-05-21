package com.itmo.db.generator.persistence;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.PersonProjectLink;
import com.itmo.db.generator.persistence.db.mysql.repository.*;
import com.itmo.db.generator.persistence.db.oracle.dao.PersonOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.repository.GroupOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.PersonOracleRepository;
import com.itmo.db.generator.persistence.db.postgres.repository.*;
import com.itmo.db.generator.persistence.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersistenceWorkerFactory {

    @Autowired
    private Generator generator;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonOracleRepository personOracleRepository;

    @Autowired
    private GroupOracleRepository groupOracleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PersonProjectLinkRepository personProjectRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UniversityRepository universityRepository;

    public PersistenceWorkerFactory() {
    }

    public <T extends AbstractEntity<TId>, TId> PersistenceWorker getWorker(Class<T> entityClass) {
        if (entityClass.equals(Person.class)) {
            return new PersonPersistenceWorker(generator, personRepository, personOracleRepository);
        } else if (entityClass.equals(Project.class)) {
            return new ProjectPersistenceWorker(generator, projectRepository);
        } else if (entityClass.equals(PersonProjectLink.class)) {
            return new PersonProjectLinkPersistenceWorker(generator, personProjectRepository);
        } else if (entityClass.equals(Faculty.class)) {
            return new FacultyPersistenceWorker(generator, facultyRepository);
        } else if (entityClass.equals(Issue.class)) {
            return new IssuePersistenceWorker(generator, issueRepository);
        } else if (entityClass.equals(Conference.class)) {
            return new ConferencePersistenceWorker(generator, conferenceRepository);
        } else if (entityClass.equals(Publication.class)) {
            return new PublicationPersistenceWorker(generator, publicationRepository);
        } else if (entityClass.equals(Discipline.class)) {
            return new DisciplinePersistenceWorker(generator, disciplineRepository);
        } else if (entityClass.equals(University.class)) {
            return new UniversityPersistenceWorker(generator, universityRepository);
        } else if (entityClass.equals(Group.class)) {
            return new GroupPersistenceWorker(generator, groupOracleRepository);
        }

        throw new NullPointerException();
    }

}
