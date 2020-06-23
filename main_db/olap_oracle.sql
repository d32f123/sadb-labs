DROP TABLE time_dimension;
DROP MATERIALIZED VIEW DORM_DIMENSION;
DROP MATERIALIZED VIEW UNI_ROLE_DIMENSION;
DROP MATERIALIZED VIEW PLACE_OF_BIRTH_DIMENSION;
DROP MATERIALIZED VIEW ISSUE_DIMENSION;
DROP MATERIALIZED VIEW PUBLICATION_LANG_DIMENSION;
DROP MATERIALIZED VIEW PUBLICATION_LENGTH_DIMENSION;
DROP MATERIALIZED VIEW PUBLICATION_FACTS;
DROP MATERIALIZED VIEW PERSON_FACTS;
DROP MATERIALIZED VIEW PEOPLE_DORM_FACTS;
DROP MATERIALIZED VIEW PEOPLE_GENERAL_FACTS;

-- TIME DIMENSION --
CREATE TABLE time_dimension
(
    id        NUMBER(22, 0) PRIMARY KEY,
    semester  NUMBER(22, 0),
    year      number(22, 0),
    year_date date
);

DECLARE
    i integer;
BEGIN
    FOR i in 0..66
        LOOP
            INSERT INTO time_dimension
            VALUES (i,
                    MOD(i, 2) + 1,
                    1990 + FLOOR(i / 2),
                    TO_DATE(TO_CHAR(MOD(i, 2) * 6 + 1) || '-' || TO_CHAR(1990 + FLOOR(i / 2)), 'MM-YYYY'));
        end loop;
    commit;
end;
-- END TIME DIMENSION --

-- DORM DIMENSION --
CREATE MATERIALIZED VIEW DORM_DIMENSION AS
SELECT ID, ADDRESS
FROM DORMITORY;
commit;

-- Update dorm_dimension view
BEGIN
    DBMS_MVIEW.REFRESH('dorm_dimension');
end;
-- END DORM DIMENSION --

-- UNI ROLE DIMENSION --
CREATE MATERIALIZED VIEW UNI_ROLE_DIMENSION AS
SELECT ROWNUM  ID,
       ROLE as ROLE_NAME
FROM (SELECT ROLE FROM PERSON GROUP BY ROLE ORDER BY ROLE);

-- Update uni_role_dimension view
BEGIN
    DBMS_MVIEW.REFRESH('uni_role_dimension');
end;
-- END UNI ROLE DIMENSION

-- PLACE OF BIRTH DIMENSION --
CREATE MATERIALIZED VIEW PLACE_OF_BIRTH_DIMENSION AS
SELECT ROWNUM                                                                              AS ID,
       BIRTHPLACE                                                                          as CITY,
       (CASE WHEN RAND < 5 THEN 'Ukraine' WHEN RAND < 15 THEN 'Belarus' ELSE 'Russia' END) as COUNTRY
FROM (
         SELECT BIRTHPLACE, (DBMS_RANDOM.VALUE(0, 100)) as RAND
         FROM PERSON
         GROUP BY BIRTHPLACE
         ORDER BY BIRTHPLACE
     );


-- Update place of birth dimension view
BEGIN
    DBMS_MVIEW.REFRESH('place_of_birth_dimension');
end;
-- END PLACE OF BIRTH DIMENSION --

-- ISSUE DIMENSION --
CREATE MATERIALIZED VIEW ISSUE_DIMENSION AS
SELECT ROWNUM AS ID,
       FORMAT as NAME
FROM (SELECT FORMAT FROM ISSUE GROUP BY FORMAT ORDER BY FORMAT);
-- END ISSUE DIMENSION --

-- PUBLICATION LANGUAGE DIMENSION --
CREATE MATERIALIZED VIEW PUBLICATION_LANG_DIMENSION AS
SELECT ROWNUM   AS ID,
       LANGUAGE AS LANGUAGE
FROM (SELECT LANGUAGE FROM PUBLICATION GROUP BY LANGUAGE ORDER BY LANGUAGE);
-- END PUBLICATION LANGUAGE

-- PUBLICATION LENGTH DIMENSION --
CREATE MATERIALIZED VIEW PUBLICATION_LENGTH_DIMENSION AS
SELECT ROWNUM AS ID,
       LENGTH AS LENGTH
FROM (SELECT LENGTH FROM ISSUE GROUP BY LENGTH ORDER BY LENGTH);
-- END PUBLICATION LENGTH DIMENSION --

-- PUBLICATION FACTS --
CREATE MATERIALIZED VIEW PUBLICATION_FACTS AS
SELECT COUNT(pubs.person_id) as person_count,
       issue_d.ID              as issue_id,
       pub_lang_d.ID           as pub_lang_id,
       pub_len_d.id            as pub_len_id,
       time_d.id               as time_id
FROM ISSUE_DIMENSION issue_d,
     PUBLICATION_LANG_DIMENSION pub_lang_d,
     PUBLICATION_LENGTH_DIMENSION pub_len_d,
     TIME_DIMENSION time_d,
     (
        SELECT issue_d1.ID issue_d_id,
               pub_lang_d1.ID pub_lang_d_id,
               pub_len_d1.ID pub_len_d_id,
               time_d1.ID time_d_id,
               pers1.ID person_id
        FROM ISSUE_DIMENSION issue_d1, PUBLICATION_LANG_DIMENSION pub_lang_d1, PUBLICATION_LENGTH_DIMENSION pub_len_d1,
             TIME_DIMENSION time_d1, PERSON pers1, PUBLICATION pub1, PERSONPUBLICATIONLINK pers_pub1,
             ISSUE iss1, ISSUEPUBLICATIONLINK iss_pub1
        WHERE pers1.ID = pers_pub1.PERSONID AND
              pers_pub1.PUBLICATIONID = pub1.ID AND
              pub1.ID = iss_pub1.PUBLICATIONID AND
              iss_pub1.ISSUEID = iss1.ID AND
              time_d1.YEAR = EXTRACT(YEAR FROM pub1.PUBLICATIONDATE) AND
              time_d1.semester = (FLOOR(EXTRACT(MONTH FROM pub1.PUBLICATIONDATE) / 6) + 1) AND
              pub_lang_d1.LANGUAGE = pub1.LANGUAGE AND
              pub_len_d1.LENGTH = iss1.LENGTH AND
              issue_d1.NAME = iss1.FORMAT
        GROUP BY issue_d1.ID, pub_lang_d1.ID, pub_len_d1.ID, time_d1.ID, pers1.ID
     ) pubs
WHERE pubs.issue_d_id (+)= issue_d.ID AND
      pubs.pub_lang_d_id (+)= pub_lang_d.ID AND
      pubs.pub_len_d_id (+)= pub_len_d.ID AND
      pubs.time_d_id (+)= time_d.ID
GROUP BY issue_d.id, pub_lang_d.id, pub_len_d.id, time_d.id
ORDER BY issue_d.id, pub_lang_d.id, pub_len_d.id, time_d.id;
-- END PUBLICATION FACTS --

-- PERSON FACTS --
-- place of birth and uni role
CREATE MATERIALIZED VIEW PERSON_FACTS AS
SELECT COUNT(person_aca.person_id) as person_count,
       pob_d.ID                as pob_id_id,
       uni_role_d.ID           as uni_role_id,
       time_d.ID               as time_d_id
FROM PLACE_OF_BIRTH_DIMENSION pob_d,
     UNI_ROLE_DIMENSION uni_role_d,
     TIME_DIMENSION time_d,
     (
        SELECT pob_d1.id pob_id,
               uni_role_d1.id uni_role_id,
               time_d1.id time_id,
               person1.ID person_id
        FROM PLACE_OF_BIRTH_DIMENSION pob_d1, UNI_ROLE_DIMENSION uni_role_d1,
             TIME_DIMENSION time_d1, PERSON person1, ACADEMICRECORD aca_rec1
        WHERE person1.BIRTHPLACE = pob_d1.CITY AND
              person1.ROLE = uni_role_d1.ROLE_NAME AND
              aca_rec1.PERSONID = person1.ID AND
              EXTRACT(YEAR FROM aca_rec1.ACADEMICYEAR) = time_d1.YEAR
        GROUP BY pob_d1.id, uni_role_d1.id, time_d1.id, person1.ID
     ) person_aca
WHERE person_aca.pob_id (+)= pob_d.ID AND
      person_aca.uni_role_id (+)= uni_role_d.ID AND
      person_aca.time_id (+)= time_d.ID
GROUP BY pob_d.ID, uni_role_d.ID, time_d.ID
ORDER BY pob_d.ID, uni_role_d.ID, time_d.ID;
-- END PERSON FACTS --

-- PEOPLE DORMITORY FACTS --
CREATE MATERIALIZED VIEW PEOPLE_DORM_FACTS AS
SELECT AVG(room_people_cnt.N_PEOPLE)                                                  as n_people,
       COUNT(CASE WHEN student_time_min_marks.MIN_MARK = 5 THEN 1 END)                as n_excellent,
       COUNT(CASE WHEN student_time_min_marks.MIN_MARK = 4 THEN 1 END)                as n_good,
       COUNT(CASE WHEN student_time_min_marks.MIN_MARK = 3 THEN 1 END)                as n_fair,
       COUNT(CASE student_time_min_marks.MIN_MARK WHEN 2 THEN 1 WHEN NULL THEN 1 END) as n_poor,
       dorm_d.ID                                                                      as dorm_d_id,
       time_d.ID                                                                      as time_d_id
FROM DORMITORY dorm,
     ROOM room,
     ACCOMMODATIONRECORD acc_rec,
     PERSON person,
     STUDENTSEMESTERDISCIPLINE ssd,
     DORM_DIMENSION dorm_d,
     TIME_DIMENSION time_d,
     (
         SELECT room1.ID                                    as ROOM_ID,
                EXTRACT(YEAR FROM acc_rec1.LIVINGSTARTDATE) as YEAR,
                COUNT(DISTINCT acc_rec1.ID)                 as N_PEOPLE
         FROM ROOM room1,
              ACCOMMODATIONRECORD acc_rec1
         WHERE room1.ID = acc_rec1.ROOMID
         GROUP BY room1.ID, EXTRACT(YEAR FROM acc_rec1.LIVINGSTARTDATE)
     ) room_people_cnt,
     (
         SELECT student1.ID                       as ID,
                EXTRACT(YEAR FROM ssd1.SCOREDATE) as YEAR,
                MIN(ssd1.MARK)                    as MIN_MARK
         FROM PERSON student1,
              STUDENTSEMESTERDISCIPLINE ssd1
         WHERE student1.ID = ssd1.STUDENTID
           AND ssd1.SCORE IS NOT NULL
         GROUP BY student1.ID, EXTRACT(YEAR FROM ssd1.SCOREDATE)
     ) student_time_min_marks
WHERE dorm.ID = room.DORMITORYID
  AND room.ID = acc_rec.ROOMID
  AND acc_rec.PERSONID (+) = person.ID
  AND ssd.STUDENTID = person.ID
  AND room_people_cnt.ROOM_ID = room.ID
  AND student_time_min_marks.ID = person.ID
  AND dorm_d.ADDRESS = dorm.ADDRESS
  AND time_d.YEAR = EXTRACT(YEAR FROM acc_rec.LIVINGSTARTDATE)
  AND time_d.SEMESTER = (FLOOR(EXTRACT(MONTH FROM acc_rec.LIVINGSTARTDATE) / 6) + 1)
  AND time_d.YEAR = room_people_cnt.YEAR
  AND time_d.YEAR = student_time_min_marks.YEAR
GROUP BY dorm_d.ID, time_d.ID
ORDER BY dorm_d.ID, time_d.ID;
-- END PEOPLE DORMITORY FACTS --

-- PEOPLE GENERAL FACTS --
CREATE MATERIALIZED VIEW PEOPLE_GENERAL_FACTS AS
SELECT time_d.ID                                                                       as time_d_id,
       n_confs.n_confs                                                                 as n_conferences,
       n_pubs.n_pubs                                                                   as n_publications,
       SUM(person_ar.is_in_dorm)                                                       as dorm_livers,
       COUNT(CASE person_ar.is_in_dorm WHEN 0 THEN 1 END)                              as non_dorm_livers,
       COUNT(CASE marks.pers_mark WHEN 5 THEN 1 END)                                   as excellent_diplomas,
       COUNT(DECODE(marks.time_id, NULL, NULL, (DECODE(marks.pers_mark, 5, NULL, 1)))) as normal_diplomas,
       read_docent.prof_n                                                              as prof_read_n,
       read_stud.stud_n                                                                as stud_read_n
FROM (
         SELECT pers1.ID                                              as id,
                time_d.ID                                             as time_id,
                (CASE WHEN COUNT(ar1.PERSONID) > 0 THEN 1 ELSE 0 END) as is_in_dorm
         FROM TIME_DIMENSION time_d,
              PERSON pers1,
              ACADEMICRECORD ac_rec1,
              ACCOMMODATIONRECORD ar1
         WHERE pers1.ID = ac_rec1.PERSONID
           AND ar1.PERSONID(+) = pers1.ID
           AND (
                     EXTRACT(YEAR FROM ac_rec1.ACADEMICYEAR) = EXTRACT(YEAR FROM ar1.LIVINGSTARTDATE)
                 OR ar1.LIVINGSTARTDATE IS NULL
             )
           AND time_d.YEAR = EXTRACT(YEAR FROM ac_rec1.ACADEMICYEAR)
         GROUP BY time_d.ID, pers1.ID
     ) person_ar,   -- can be inner joined, no semester
     (
         SELECT time_d2.ID                  as time_id,
                pers2.id                    as pers_id,
                ROUND(AVG(ssd2.MARK) + 0.4) as pers_mark
         FROM time_dimension time_d2,
              PERSON pers2,
              STUDENTSEMESTERDISCIPLINE ssd2
         WHERE time_d2.YEAR = EXTRACT(YEAR FROM ssd2.MARKDATE)
           AND time_d2.SEMESTER = (FLOOR(EXTRACT(MONTH FROM ssd2.MARKDATE) / 6) + 1)
           AND ssd2.STUDENTID = pers2.ID
         GROUP BY time_d2.ID, pers2.ID
     ) marks,       -- can be inner joined
     (
         SELECT time_d6.ID as time_id, COUNT(pub6.ID) as n_pubs
         FROM PUBLICATION pub6,
              time_dimension time_d6
         WHERE (FLOOR(EXTRACT(MONTH FROM pub6.PUBLICATIONDATE) / 6) + 1) = time_d6.semester
           AND EXTRACT(YEAR FROM pub6.PUBLICATIONDATE) = time_d6.year
         GROUP BY time_d6.ID
     ) n_pubs,
     (
         SELECT time_d7.ID as time_id, COUNT(conf7.ID) as n_confs
         FROM CONFERENCE conf7,
              time_dimension time_d7
         WHERE (FLOOR(EXTRACT(MONTH FROM conf7.CONFERENCEDATE) / 6) + 1) = time_d7.semester
           AND EXTRACT(YEAR FROM conf7.CONFERENCEDATE) = time_d7.year
         GROUP BY time_d7.ID
     ) n_confs,
     (
         SELECT time_d3.ID as time_id, COUNT(pers3.ID) AS stud_n
         FROM TIME_DIMENSION time_d3,
              PERSON pers3,
              LIBRARYRECORD lr3
         WHERE pers3.ID = lr3.PERSONID
           AND pers3.ROLE != 'docent'
           AND EXTRACT(YEAR FROM lr3.ACTIONDATE) = time_d3.year
           AND (FLOOR(EXTRACT(MONTH FROM lr3.ACTIONDATE) / 6) + 1) = time_d3.semester
         GROUP BY time_d3.id
         ORDER BY time_d3.id
     ) read_stud,   -- can be inner joined, count those that are not null
     (
         SELECT time_d3.ID as time_id, COUNT(pers3.ID) AS prof_n
         FROM TIME_DIMENSION time_d3,
              PERSON pers3,
              LIBRARYRECORD lr3
         WHERE pers3.ID = lr3.PERSONID
           AND pers3.ROLE = 'docent'
           AND EXTRACT(YEAR FROM lr3.ACTIONDATE) = time_d3.year
           AND (FLOOR(EXTRACT(MONTH FROM lr3.ACTIONDATE) / 6) + 1) = time_d3.semester
         GROUP BY time_d3.id
         ORDER BY time_d3.id
     ) read_docent, -- can be inner joined, count those that are not null
     PERSON pers,
     TIME_DIMENSION time_d
WHERE n_pubs.time_id (+) = time_d.id
  AND n_confs.time_id (+) = time_d.id
  AND marks.time_id (+) = time_d.id
  AND marks.pers_id (+) = pers.ID
  AND person_ar.id (+) = pers.ID
  AND person_ar.time_id (+) = time_d.id
  AND read_docent.time_id (+) = time_d.id
  AND read_stud.time_id (+) = time_d.id
GROUP BY time_d.ID, n_pubs.n_pubs, n_confs.n_confs, read_docent.prof_n, read_stud.stud_n
ORDER BY time_d.ID;
-- END PEOPLE GENERAL FACTS --


SELECT * FROM PEOPLE_GENERAL_FACTS;
SELECT * FROM PEOPLE_DORM_FACTS;
SELECT * FROM PERSON_FACTS;
SELECT * FROM PUBLICATION_FACTS;
SELECT * FROM conference;
SELECT * FROM PUBLICATION;

UPDATE CONFERENCE SET CONFERENCEDATE = TO_DATE(TO_CHAR(ROUND(DBMS_RANDOM.value(2000, 2010))), 'YYYY');
UPDATE PUBLICATION SET PUBLICATIONDATE = TO_DATE(TO_CHAR(ROUND(DBMS_RANDOM.value(2000, 2010))), 'YYYY');
commit;

BEGIN
    DBMS_MVIEW.REFRESH_ALL();
end;

