drop table if exists student_semester_disciplines;
drop table if exists specialty_disciplines;
drop table if exists disciplines;
drop table if exists student_infos;
drop table if exists persons;
drop table if exists specialties;
drop table if exists semesters;
drop table if exists faculties;

create table faculties
(
    faculty_id   serial primary key,
    faculty_name text unique not null
);

create table semesters
(
    semester_id serial primary key
);

create table specialties
(
    specialty_id   serial primary key,
    specialty_name text unique not null
);

create table persons
(
    person_id       serial primary key,
    last_name       text not null,
    first_name      text not null,
    patronymic_name text,
    university_role text not null,
    faculty_id      int  not null references faculties (faculty_id)
);

create table student_infos
(
    person_id    int primary key references persons (person_id),
    specialty_id int  not null references specialties (specialty_id),
    study_type   text not null
);

create table disciplines
(
    discipline_id  serial primary key,
    study_type     text not null,
    control_form   text not null,
    study_standard text not null,
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
    student_id       int       not null references persons (person_id),
    semester_id      int       not null references semesters (semester_id),
    discipline_id    int       not null references disciplines (discipline_id),
    professor_id     int       not null references persons (person_id),
    semester_counter int       not null,
    score            int       not null,
    score_date       timestamp not null default current_timestamp,
    primary key (student_id, semester_id, discipline_id)
);
