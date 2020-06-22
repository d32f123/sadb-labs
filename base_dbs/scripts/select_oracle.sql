-- -632802873 -- AcademicRecord
-- 417331327 -- Discipline
-- -1681101808 -- Group
-- -328804380 -- Person
-- 603761503 -- PersonGroupLink
-- -1344165004 -- StudentSemesterDiscipline
-- -690959282 -- StudentSemesterDiscipline key
-- -706045097 -- ScheduleRecord

SELECT 
	io.ID AS object_id,
	ia.NAME AS "FIELD_TYPE",
	COALESCE(ip.VALUE, re.NAME, TO_CHAR(ip.LIST_VALUE_ID)) AS VALUE,
	(CASE WHEN ip.REFERENCE_ID IS NULL THEN 'NO' ELSE TO_CHAR(ip.REFERENCE_ID) END) AS IS_REFERENCE
FROM ITMO_OBJECT_TYPES iot, 
	ITMO_ATTRIBUTES ia,
	ITMO_OBJECTS io,
	ITMO_PARAMS ip,
	ITMO_OBJECTS re
WHERE
	iot.ID = -706045097 AND -- Object Type (see above)
	--io.ID = 288 AND
	io.OBJECT_TYPE_ID = iot.ID AND 
	ip.OBJECT_ID = io.ID AND 
	ia.ID = ip.ATTRIBUTE_ID AND
	re.ID (+)= ip.REFERENCE_ID 
ORDER BY io.id;
