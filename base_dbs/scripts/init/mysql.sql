create database if not exists db_itmo;

use db_itmo;

drop table if exists library_records;
drop table if exists conference_publications;
drop table if exists person_projects;
drop table if exists person_publications;
drop table if exists issue_publications;
drop table if exists projects;
drop table if exists issues;
drop table if exists conferences;
drop table if exists publications;
drop table if exists persons;

create table persons
(
    person_id       SERIAL,
    last_name       VARCHAR(60) NOT NULL,
    first_name      VARCHAR(60) NOT NULL,
    patronymic_name VARCHAR(60),
    role            VARCHAR(20) NOT NULL
);

create table projects
(
    project_id SERIAL,
    name       VARCHAR(127) NOT NULL
);

create table publications
(
    publication_id SERIAL,
    name           VARCHAR(15) NOT NULL,
    language       VARCHAR(3)  NOT NULL DEFAULT 'RU',
    citation_index INT         NOT NULL DEFAULT 0,
    date           TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table issues
(
    issue_id SERIAL,
    name     VARCHAR(127) NOT NULL,
    language VARCHAR(3)   NOT NULL DEFAULT 'RU',
    location VARCHAR(255) NOT NULL,
    length   INT          NOT NULL,
    format   VARCHAR(60)  NOT NULL
);

create table conferences
(
    conference_id SERIAL,
    name          VARCHAR(127) NOT NULL,
    location      VARCHAR(255) NOT NULL,
    date          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table person_publications
(
    person_id      BIGINT UNSIGNED NOT NULL,
    publication_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (person_id, publication_id),
    FOREIGN KEY perpub_person_fk (person_id) REFERENCES persons (person_id),
    FOREIGN KEY perpub_publication_fk (publication_id) REFERENCES publications (publication_id)
);

create table person_projects
(
    person_id           BIGINT UNSIGNED NOT NULL,
    project_id          BIGINT UNSIGNED NOT NULL,
    participation_start TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    participation_end   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (person_id, project_id),
    FOREIGN KEY perpro_person_fk (person_id) REFERENCES persons (person_id),
    FOREIGN KEY perpro_project_fk (project_id) REFERENCES projects (project_id)
);

create table issue_publications
(
    issue_id       BIGINT UNSIGNED NOT NULL,
    publication_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (issue_id, publication_id),
    FOREIGN KEY isspub_issue_fk (issue_id) REFERENCES issues (issue_id),
    FOREIGN KEY isspub_publication_fk (publication_id) REFERENCES publications (publication_id)

);

create table conference_publications
(
    conference_id  BIGINT UNSIGNED NOT NULL,
    publication_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (conference_id, publication_id),
    FOREIGN KEY conpub_conference_fk (conference_id) REFERENCES conferences (conference_id),
    FOREIGN KEY conpub_publication_fk (publication_id) REFERENCES publications (publication_id)
);

create table library_records
(
    library_record_id SERIAL,
    person_id         BIGINT UNSIGNED NOT NULL,
    book_id           VARCHAR(255)    NOT NULL,
    action            VARCHAR(10)     NOT NULL,
    action_date       DATE            NOT NULL
);
