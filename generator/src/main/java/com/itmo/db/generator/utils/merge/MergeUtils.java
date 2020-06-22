package com.itmo.db.generator.utils.merge;

import com.itmo.db.generator.generator.model.DependencyDefinition;
import com.itmo.db.generator.generator.model.EntityDefinition;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.*;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.*;
import com.itmo.db.generator.persistence.db.oracle.dao.ItmoParamOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoAttributeOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoObjectOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoObjectTypeOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoParamOracleRepository;
import com.itmo.db.generator.persistence.db.postgres.dao.DisciplinePostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.SemesterPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.StudentPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.StudentSemesterDisciplinePostgresDAO;
import com.itmo.db.generator.utils.dependencytree.DependencyTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MergeUtils {

    @Autowired // yes
            ItmoAttributeOracleRepository itmoAttributeOracleRepository;
    @Autowired
    ItmoObjectOracleRepository itmoObjectOracleRepository;
    @Autowired
    ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository;
    @Autowired
    ItmoParamOracleRepository itmoParamOracleRepository;

    public Map<Class<? extends IdentifiableDAO>, HashMap<Object, Object>> oldNewObjectsIdMap;
    public Map<Class<? extends AbstractEntity>, HashMap<Long, Object>> oldNewOracleObjectsIdMap;



    public Method findSetter(Class clazz, String fieldName) {
        PropertyDescriptor pd;
        try {
            if (log.isTraceEnabled()) {
                log.trace("Trying to get setter for property with name '{}'", fieldName);
            }
            pd = new PropertyDescriptor(fieldName, clazz);
            return pd.getWriteMethod();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Method findGetter(Class clazz, String fieldName) {
        PropertyDescriptor pd;
        try {
            if (log.isTraceEnabled()) {
                log.trace("Trying to get setter for property with name '{}'", fieldName);
            }
            pd = new PropertyDescriptor(fieldName, clazz);
            return pd.getReadMethod();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.warn("Could not find getter in '{}#{}'", clazz.getSimpleName(), fieldName);
        }
        return null;
    }

    public Function getConverter(Class<?> source, Class<?> target) {
        if (target.isAssignableFrom(source)) {
            return (x) -> x;
        }
        if (Character.class.equals(target)) {
            if (String.class.equals(source)) {
                return (Function<String, Character>) x -> x.charAt(0);
            }
        }
        if (Short.class.equals(target)) {
            if (Integer.class.equals(source)) {
                return (Function<Integer, Short>) Integer::shortValue;
            }
            if (String.class.equals(source)) {
                return (Function<String, Short>) Short::valueOf;
            }
        }
        if (Integer.class.equals(target)) {
            if (Long.class.equals(source)) {
                return (Function<Long, Integer>) Long::intValue;
            }
            if (String.class.equals(source)) {
                return (Function<String, Integer>) Integer::valueOf;
            }
        }
        if (LocalDate.class.equals(target)) {
            if (CharSequence.class.isAssignableFrom(source)) {
                return (Function<CharSequence, LocalDate>) LocalDate::parse;
            }
            if (Date.class.equals(source)) {
                return (Function<Date, LocalDate>) (x) -> x.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
            }
            if (Timestamp.class.equals(source)) {
                return (Function<Timestamp, LocalDate>) (x) -> x.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
            }
            if (java.sql.Date.class.equals(source)) {
                return (Function<java.sql.Date, LocalDate>) java.sql.Date::toLocalDate;
            }
        }
        if (LocalTime.class.equals(target)) {
            if (CharSequence.class.isAssignableFrom(source)) {
                return (Function<CharSequence, LocalTime>) LocalTime::parse;
            }
        }
        if (Boolean.class.equals(target)) {
            if (String.class.equals(source)) {
                return (Function<String, Boolean>) Boolean::getBoolean;
            }
        }
        if (PersonGroupLink.PersonGroupLinkPK.class.equals(target)) {
            if (Long.class.equals(source)) {
                return (Function<Long, PersonGroupLink.PersonGroupLinkPK>) (x) -> {
                    var keys = getOracleCompositeKey(x);

                    return new PersonGroupLink.PersonGroupLinkPK(
                            (int) this.oldNewOracleObjectsIdMap.get(Person.class).get(keys.get(0).getReferenceId()),
                            (int) this.oldNewOracleObjectsIdMap.get(Group.class).get(keys.get(1).getReferenceId())
                    );
                };
            }
        }
        if (PersonPublicationLink.PersonPublicationLinkPK.class.equals(target)) {
            if (PersonPublicationLinkMySQLDAO.PersonPublicationLinkPK.class.equals(source)) {
                return (Function<PersonPublicationLinkMySQLDAO.PersonPublicationLinkPK, PersonPublicationLink.PersonPublicationLinkPK>) (x) ->
                        new PersonPublicationLink.PersonPublicationLinkPK(
                                (int) this.oldNewObjectsIdMap.get(PersonMySQLDAO.class).get(x.getPersonId()),
                                (int) this.oldNewObjectsIdMap.get(PublicationMySQLDAO.class).get(x.getPublicationId())
                        );
            }
        }
        if (PersonProjectLink.PersonProjectLinkPK.class.equals(target)) {
            if (PersonProjectLinkMySQLDAO.PersonProjectLinkPK.class.equals(source)) {
                return (Function<PersonProjectLinkMySQLDAO.PersonProjectLinkPK, PersonProjectLink.PersonProjectLinkPK>) (x) ->
                        new PersonProjectLink.PersonProjectLinkPK(
                                (int) this.oldNewObjectsIdMap.get(PersonMySQLDAO.class).get(x.getPersonId()),
                                (int) this.oldNewObjectsIdMap.get(ProjectMySQLDAO.class).get(x.getProjectId())
                        );
            }
        }
        if (IssuePublicationLink.IssuePublicationLinkPK.class.equals(target)) {
            if (IssuePublicationLinkMySQLDAO.IssuePublicationLinkPK.class.equals(source)) {
                return (Function<IssuePublicationLinkMySQLDAO.IssuePublicationLinkPK, IssuePublicationLink.IssuePublicationLinkPK>) (x) ->
                        new IssuePublicationLink.IssuePublicationLinkPK(
                                (int) this.oldNewObjectsIdMap.get(IssueMySQLDAO.class).get(x.getIssueId()),
                                (int) this.oldNewObjectsIdMap.get(PublicationMySQLDAO.class).get(x.getPublicationId())
                        );
            }
        }
        if (ConferencePublicationLink.ConferencePublicationLinkPK.class.equals(target)) {
            if (ConferencePublicationLinkMySQLDAO.ConferencePublicationLinkPK.class.equals(source)) {
                return (Function<ConferencePublicationLinkMySQLDAO.ConferencePublicationLinkPK, ConferencePublicationLink.ConferencePublicationLinkPK>) (x) ->
                        new ConferencePublicationLink.ConferencePublicationLinkPK(
                                (int) this.oldNewObjectsIdMap.get(ConferenceMySQLDAO.class).get(x.getConferenceId()),
                                (int) this.oldNewObjectsIdMap.get(PublicationMySQLDAO.class).get(x.getPublicationId())
                        );
            }
        }
        if (StudentSemesterDiscipline.StudentSemesterDisciplinePK.class.equals(target)) {
            if (StudentSemesterDisciplinePostgresDAO.StudentSemesterDisciplinePostgresPK.class.equals(source)) {
                return (Function<StudentSemesterDisciplinePostgresDAO.StudentSemesterDisciplinePostgresPK, StudentSemesterDiscipline.StudentSemesterDisciplinePK>) (x) ->
                        new StudentSemesterDiscipline.StudentSemesterDisciplinePK(
                                (int) this.oldNewObjectsIdMap.get(StudentPostgresDAO.class).get(x.getStudentId()),
                                (int) this.oldNewObjectsIdMap.get(DisciplinePostgresDAO.class).get(x.getDisciplineId()),
                                (int) this.oldNewObjectsIdMap.get(SemesterPostgresDAO.class).get(x.getSemesterId())
                        );
            }
            if (Long.class.equals(source)) {
                return (Function<Long, StudentSemesterDiscipline.StudentSemesterDisciplinePK>) (x) -> {
                    var keys = getOracleCompositeKey(x);
                    return new StudentSemesterDiscipline.StudentSemesterDisciplinePK(
                            (int) this.oldNewOracleObjectsIdMap.get(Person.class).get(keys.get(0).getReferenceId()),
                            (int) this.oldNewOracleObjectsIdMap.get(Discipline.class).get(keys.get(1).getReferenceId()),
                            Integer.parseInt(keys.get(2).getValue())
                    );
                };
            }
        }

        log.error("Error during receiving converter from '{}' to '{}'", source.getSimpleName(), target.getSimpleName());
        return null;
    }

    public List<ItmoParamOracleDAO> getOracleCompositeKey(Long keyId) {
        List<ItmoParamOracleDAO> keys = itmoParamOracleRepository.findByObjectId(keyId);
        keys.sort(Comparator.comparing(ItmoParamOracleDAO::getId));
        return keys;
    }

    public void setValueWithSpecifiedMethods(Object instance, Method setter, Object arg, Function converter)
            throws InvocationTargetException, IllegalAccessException {
        try {
            if (arg == null || arg.equals("null"))
                return;
            setter.invoke(instance, converter.apply(arg));
        } catch (Exception e) {
            log.error("Failed to set arg '{}' to setter '{}' of instance '{}'", arg, setter, instance);
            throw e;
        }
    }

    public List<Set<Class<? extends AbstractEntity<?>>>> getEntities() {
        int baseAmount = 0;
        List<Set<EntityDefinition<?, ?>>> list = new DependencyTree(Set.of(
                new EntityDefinition<>(AcademicRecord.class, null, Set.of(
                        new DependencyDefinition<>(Person.class, 1)
                )),
                new EntityDefinition<>(AccommodationRecord.class, null, Set.of(
                        new DependencyDefinition<>(Person.class, 2),
                        new DependencyDefinition<>(Room.class, 1)
                )),
                new EntityDefinition<>(Conference.class, baseAmount, null),
                new EntityDefinition<>(ConferencePublicationLink.class, null, Set.of(
                        new DependencyDefinition<>(Conference.class, 1),
                        new DependencyDefinition<>(Publication.class, 5)
                )),
                new EntityDefinition<>(Discipline.class, 0, null),
                new EntityDefinition<>(Dormitory.class, 0, null),
                new EntityDefinition<>(Faculty.class, null, Set.of(
                        new DependencyDefinition<>(University.class, 1)
                )),
                new EntityDefinition<>(Group.class, baseAmount, null),
                new EntityDefinition<>(Issue.class, baseAmount, null),
                new EntityDefinition<>(IssuePublicationLink.class, null, Set.of(
                        new DependencyDefinition<>(Issue.class, 1),
                        new DependencyDefinition<>(Publication.class, 3)
                )),
                new EntityDefinition<>(LibraryRecord.class, null, Set.of(
                        new DependencyDefinition<>(Person.class, 1)
                )),
                new EntityDefinition<>(Person.class, baseAmount, null),
                new EntityDefinition<>(PersonGroupLink.class, null, Set.of(
                        new DependencyDefinition<>(Person.class, 15),
                        new DependencyDefinition<>(Group.class, 1)
                )),
                new EntityDefinition<>(PersonProjectLink.class, null, Set.of(
                        new DependencyDefinition<>(Person.class, 1),
                        new DependencyDefinition<>(Project.class, 4)
                )),
                new EntityDefinition<>(PersonPublicationLink.class, null, Set.of(
                        new DependencyDefinition<>(Person.class, 1),
                        new DependencyDefinition<>(Publication.class, 2)
                )),
                new EntityDefinition<>(Professor.class, null, Set.of(
                        new DependencyDefinition<>(Person.class, 1),
                        new DependencyDefinition<>(Faculty.class, 1)
                )),
                new EntityDefinition<>(Project.class, baseAmount, null),
                new EntityDefinition<>(Publication.class, baseAmount, null),
                new EntityDefinition<>(Room.class, null, Set.of(
                        new DependencyDefinition<>(Dormitory.class, 1)
                )),
                new EntityDefinition<>(ScheduleRecord.class, null, Set.of(
                        new DependencyDefinition<>(StudentSemesterDiscipline.class, 1)
                )),
                new EntityDefinition<>(Semester.class, baseAmount, null),
                new EntityDefinition<>(Specialty.class, null, Set.of(
                        new DependencyDefinition<>(Faculty.class, 1)
                )),
                new EntityDefinition<>(SpecialtyDisciplineLink.class, null, Set.of(
                        new DependencyDefinition<>(Specialty.class, 1),
                        new DependencyDefinition<>(Discipline.class, 6)
                )),
                new EntityDefinition<>(Student.class, null, Set.of(
                        new DependencyDefinition<>(Person.class, 15),
                        new DependencyDefinition<>(Specialty.class, 1)
                )),
                new EntityDefinition<>(StudentSemesterDiscipline.class, null, Set.of(
                        new DependencyDefinition<>(Student.class, 10),
                        new DependencyDefinition<>(Semester.class, 2),
                        new DependencyDefinition<>(Discipline.class, 1),
                        new DependencyDefinition<>(Professor.class, 2)
                )),
                new EntityDefinition<>(University.class, 0, null)
        )).getLeveledEntities();

        List<Set<Class<? extends AbstractEntity<?>>>> retList = new ArrayList<>();
        list.forEach(item -> retList.add(
                item.stream().map((Function<EntityDefinition, Class<? extends AbstractEntity<?>>>) EntityDefinition::getEntityClass).collect(Collectors.toSet())
        ));
        return retList; // ????/
    }

}
