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
    private PersonMySQLRepository personMySQLRepository;

    @Autowired
    private ProjectMySQLRepository projectMySQLRepository;

    @Autowired
    private PersonProjectLinkMySQLRepository personProjectRepository;

    @Autowired
    private PublicationMySQLRepository publicationMySQLRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private IssueMySQLRepository issueMySQLRepository;

    @Autowired
    private UniversityRepository universityRepository;

    public PersistenceWorkerFactory() {}

    public <T extends AbstractEntity<TId>, TId> PersistenceWorker getWorker(Class<T> entityClass) {
        if (entityClass.equals(Person.class)) {
            return new PersonPersistenceWorker(generator, personMySQLRepository);
        } else if (entityClass.equals(Project.class)) {
            return new ProjectPersistenceWorker(generator, projectMySQLRepository);
        } else if (entityClass.equals(PersonProjectLink.class)) {
            return new PersonProjectLinkPersistenceWorker(generator, personProjectRepository);
        } else if (entityClass.equals(Faculty.class)) {
            return new FacultyPersistenceWorker(generator, facultyRepository);
        } else if (entityClass.equals(Issue.class)) {
            return new IssuePersistenceWorker(generator, issueMySQLRepository);
        } else if (entityClass.equals(Conference.class)) {
            return new ConferencePersistenceWorker(generator, conferenceMySQLRepository);
        } else if (entityClass.equals(Publication.class)) {
            return new PublicationPersistenceWorker(generator, publicationMySQLRepository);
        } else if (entityClass.equals(Discipline.class)) {
            return new DisciplinePersistenceWorker(generator, disciplineRepository);
        } else if (entityClass.equals(University.class)) {
            return new UniversityPersistenceWorker(generator, universityRepository);
        }

        throw new NullPointerException();
    }

}
