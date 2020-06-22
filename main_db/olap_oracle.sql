DROP TABLE time_dimension;
DROP MATERIALIZED VIEW DORM_DIMENSION;
DROP MATERIALIZED VIEW UNI_ROLE_DIMENSION;
DROP MATERIALIZED VIEW PLACE_OF_BIRTH_DIMENSION;
DROP MATERIALIZED VIEW ISSUE_DIMENSION;
DROP MATERIALIZED VIEW PUBLICATION_LANG_DIMENSION;
DROP MATERIALIZED VIEW PUBLICATION_LENGTH_DIMENSION;
DROP MATERIALIZED VIEW PUBLICATION_FACTS;
DROP MATERIALIZED VIEW PERSON_FACTS;

-- TIME DIMENSION --
CREATE TABLE time_dimension (
    id          NUMBER(22, 0) PRIMARY KEY,
    semester    NUMBER(22, 0),
    year        NUMBER(22, 0)
);

DECLARE
    i integer;
BEGIN
    FOR i in 0..66 LOOP
        INSERT INTO time_dimension VALUES (i, MOD(i, 2) + 1, 1990 + FLOOR(i / 2));
    end loop;
    commit;
end;
-- END TIME DIMENSION --

-- DORM DIMENSION --
CREATE MATERIALIZED VIEW DORM_DIMENSION AS
    SELECT ID, ADDRESS FROM DORMITORY;
commit;

-- Update dorm_dimension view
BEGIN
    DBMS_MVIEW.REFRESH('dorm_dimension');
end;
-- END DORM DIMENSION --

-- UNI ROLE DIMENSION --
CREATE MATERIALIZED VIEW UNI_ROLE_DIMENSION AS
    SELECT
           ROWNUM ID,
           ROLE as ROLE_NAME
    FROM
         (SELECT ROLE FROM PERSON GROUP BY ROLE ORDER BY ROLE);

-- Update uni_role_dimension view
BEGIN
    DBMS_MVIEW.REFRESH('uni_role_dimension');
end;
-- END UNI ROLE DIMENSION

-- PLACE OF BIRTH DIMENSION --
CREATE MATERIALIZED VIEW PLACE_OF_BIRTH_DIMENSION AS
    SELECT
           ROWNUM AS ID,
           BIRTHPLACE as CITY,
           (CASE WHEN RAND < 5 THEN 'Ukraine' WHEN RAND < 15 THEN 'Belarus' ELSE 'Russia' END) as COUNTRY
    FROM
         (
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
    SELECT
           ROWNUM AS ID,
           FORMAT as NAME
    FROM
         (SELECT FORMAT FROM ISSUE GROUP BY FORMAT ORDER BY FORMAT);
-- END ISSUE DIMENSION --

-- PUBLICATION LANGUAGE DIMENSION --
CREATE MATERIALIZED VIEW PUBLICATION_LANG_DIMENSION AS
    SELECT
           ROWNUM AS ID,
           LANGUAGE AS LANGUAGE
    FROM
        (SELECT LANGUAGE FROM PUBLICATION GROUP BY LANGUAGE ORDER BY LANGUAGE);
-- END PUBLICATION LANGUAGE

-- PUBLICATION LENGTH DIMENSION --
CREATE MATERIALIZED VIEW PUBLICATION_LENGTH_DIMENSION AS
    SELECT
           ROWNUM AS ID,
           LENGTH AS LENGTH
    FROM
        (SELECT LENGTH FROM ISSUE GROUP BY LENGTH ORDER BY LENGTH);
-- END PUBLICATION LENGTH DIMENSION --

-- PUBLICATION FACTS --
CREATE MATERIALIZED VIEW PUBLICATION_FACTS AS
    SELECT COUNT(UNIQUE person.ID) as person_count,
           issue_d.ID as issue_id,
           pub_lang_d.ID as pub_lang_id,
           pub_len_d.id as pub_len_id,
           time_d.id as time_id
    FROM
         ISSUE_DIMENSION issue_d,
         PUBLICATION_LANG_DIMENSION pub_lang_d,
         PUBLICATION_LENGTH_DIMENSION pub_len_d,
         TIME_DIMENSION time_d,
         PUBLICATION pub,
         ISSUEPUBLICATIONLINK iss_pub,
         ISSUE iss,
         PERSON person,
         PERSONPUBLICATIONLINK person_pub
    WHERE
          iss_pub.ISSUEID = iss.ID AND
          iss_pub.PUBLICATIONID = pub.ID AND
          person_pub.PUBLICATIONID = pub.ID AND
          person_pub.PERSONID = person.ID AND
          issue_d.NAME = iss.FORMAT AND
          pub_lang_d.LANGUAGE = pub.LANGUAGE AND
          pub_len_d.LENGTH = iss.LENGTH AND
          time_d.YEAR = EXTRACT(YEAR FROM pub.PUBLICATIONDATE) AND
          time_d.semester = (FLOOR(EXTRACT(MONTH FROM pub.PUBLICATIONDATE) / 6) + 1)
    GROUP BY issue_d.id, pub_lang_d.id, pub_len_d.id, time_d.id
    ORDER BY issue_d.id, pub_lang_d.id, pub_len_d.id, time_d.id;
-- END PUBLICATION FACTS --

-- PERSON FACTS --
-- place of birth and uni role
CREATE MATERIALIZED VIEW PERSON_FACTS AS
    SELECT
           COUNT(UNIQUE person.ID) as person_count,
           pob_d.ID as pob_id_id,
           uni_role_d.ID as uni_role_id,
           time_d.ID as time_d_id
    FROM
         PLACE_OF_BIRTH_DIMENSION pob_d,
         UNI_ROLE_DIMENSION uni_role_d,
         TIME_DIMENSION time_d,
         PERSON person,
         ACADEMICRECORD aca_rec
    WHERE
          person.ID = aca_rec.PERSONID AND
          time_d.YEAR = EXTRACT(YEAR FROM aca_rec.ACADEMICYEAR) AND
          uni_role_d.ROLE_NAME = person.ROLE AND
          pob_d.CITY = person.BIRTHPLACE
    GROUP BY pob_d.ID, uni_role_d.ID, time_d.ID
    ORDER BY pob_d.ID, uni_role_d.ID, time_d.ID;
-- END PERSON FACTS --

-- PEOPLE DORMITORY FACTS --
DROP MATERIALIZED VIEW PEOPLE_DORM_FACTS;
CREATE MATERIALIZED VIEW PEOPLE_DORM_FACTS AS
    SELECT
           AVG(room_people_cnt.N_PEOPLE) as n_people,
           COUNT(CASE WHEN student_time_min_marks.MIN_MARK = 5 THEN 1 END) as n_excellent,
           COUNT(CASE WHEN student_time_min_marks.MIN_MARK = 4 THEN 1 END) as n_good,
           COUNT(CASE WHEN student_time_min_marks.MIN_MARK = 3 THEN 1 END) as n_fair,
           COUNT(CASE student_time_min_marks.MIN_MARK WHEN 2 THEN 1 WHEN NULL THEN 1 END) as n_poor,
           dorm_d.ID as dorm_d_id,
           time_d.ID as time_d_id
    FROM
         DORMITORY dorm,
         ROOM room,
         ACCOMMODATIONRECORD acc_rec,
         PERSON person,
         STUDENTSEMESTERDISCIPLINE ssd,
         DORM_DIMENSION dorm_d,
         TIME_DIMENSION time_d,
         (
             SELECT room1.ID as ROOM_ID,
                    EXTRACT(YEAR FROM acc_rec1.LIVINGSTARTDATE) as YEAR,
                    COUNT(DISTINCT acc_rec1.ID) as N_PEOPLE
             FROM
                  ROOM room1,
                  ACCOMMODATIONRECORD acc_rec1
             WHERE room1.ID = acc_rec1.ROOMID
             GROUP BY room1.ID, EXTRACT(YEAR FROM acc_rec1.LIVINGSTARTDATE)
         ) room_people_cnt,
         (
             SELECT
                    student1.ID as ID,
                    EXTRACT(YEAR FROM ssd1.SCOREDATE) as YEAR,
                    MIN(ssd1.MARK) as MIN_MARK
             FROM STUDENT student1, STUDENTSEMESTERDISCIPLINE ssd1
             WHERE student1.ID = ssd1.STUDENTID AND ssd1.SCORE IS NOT NULL
             GROUP BY student1.ID, EXTRACT(YEAR FROM ssd1.SCOREDATE)
         ) student_time_min_marks
    WHERE
          dorm.ID = room.DORMITORYID AND
          room.ID = acc_rec.ROOMID AND
          acc_rec.PERSONID = person.ID AND
          ssd.STUDENTID = person.ID AND
          room_people_cnt.ROOM_ID = room.ID AND
          student_time_min_marks.ID = person.ID AND
          dorm_d.ADDRESS = dorm.ADDRESS AND
          time_d.YEAR = EXTRACT(YEAR FROM acc_rec.LIVINGSTARTDATE) AND
          time_d.SEMESTER = (FLOOR(EXTRACT(MONTH FROM acc_rec.LIVINGSTARTDATE) / 6) + 1) AND
          time_d.YEAR = room_people_cnt.YEAR AND
          time_d.YEAR = student_time_min_marks.YEAR
    GROUP BY dorm_d.ID, time_d.ID
    ORDER BY dorm_d.ID, time_d.ID;

SELECT COUNT(*) FROM STUDENTSEMESTERDISCIPLINE;
SELECT * FROM PEOPLE_DORM_FACTS;
SELECT SUM(N_PEOPLE) FROM PEOPLE_DORM_FACTS;
SELECT COUNT(*) FROM STUDENT;
SELECT COUNT(*) FROM PERSON;
-- END PEOPLE DORMITORY FACTS --

SELECT * FROM person, ACADEMICRECORD where person.id = ACADEMICRECORD.PERSONID;

SELECT * FROM PERSON_FACTS;
SELECT * FROM PUBLICATION_FACTS;
SELECT * FROM PUBLICATION_LENGTH_DIMENSION;
SELECT * FROM PUBLICATION_LANG_DIMENSION;
SELECT * FROM ISSUE_DIMENSION;
SELECT * FROM UNI_ROLE_DIMENSION;
SELECT UNIQUE * FROM dorm_dimension;
SELECT * FROM time_dimension;

SELECT * FROM DORMITORY;
SELECT * FROM STUDENTSEMESTERDISCIPLINE;

truncate table time_dimension;

BEGIN
    DBMS_MVIEW.REFRESH_ALL();
end;
