-- create database db_itmo;

-- \c db_itmo;

drop table if exists student_semester_disciplines;
drop table if exists student_infos;
drop table if exists semesters;

drop table if exists professor_student_disciplines;
drop table if exists students;
drop table if exists professors;
drop table if exists specialty_disciplines;
drop table if exists disciplines;
drop table if exists persons;
drop table if exists specialties;
drop table if exists faculties;
drop table if exists universities;

create table universities
(
    university_id   serial primary key,
    name            text not null,
    creation_date   date not null default current_date
);

create table semesters
(
    semester_id     serial primary key
);

create table faculties
(
    faculty_id    serial primary key,
    university_id int not null references universities (university_id),
    faculty_name text not null
);

create table specialties
(
    specialty_id   serial primary key,
    faculty_id     int not null references faculties (faculty_id),
    study_standard text not null,
    specialty_name text not null
);

create table persons
(
    person_id       serial primary key,
    last_name       text not null,
    first_name      text not null,
    patronymic_name text,
    role            text not null,
    person_number   text not null
);

create table students
(
    person_id    int primary key references persons (person_id),
    specialty_id int  not null references specialties (specialty_id),
    study_type   text not null,
    person_number   text not null
);

create table professors
(
    person_id    int primary key references persons (person_id),
    faculty_id   int not null references faculties (faculty_id),
    person_number   text not null
);

create table disciplines
(
    discipline_id  serial primary key,
    name           text not null,
    control_form   text not null,
    lecture_hours  int  not null,
    practice_hours int  not null,
    lab_hours      int  not null
);

create table specialty_disciplines
(
    specialty_id  int not null references specialties (specialty_id),
    discipline_id int not null references disciplines (discipline_id),
    primary key (specialty_id, discipline_id)
);

create table student_semester_disciplines
(
    student_id       int       not null references students (person_id),
    semester_id      int       not null references semesters (semester_id),
    discipline_id    int       not null references disciplines (discipline_id),
    global_id        int       not null,
    professor_id     int       not null references professors (person_id),
    semester_counter int       not null,
    score            int       not null,
    score_date       timestamp not null default current_timestamp,
    primary key (student_id, semester_id, discipline_id)
);
