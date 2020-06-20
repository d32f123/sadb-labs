package com.itmo.db.generator;

import com.itmo.db.generator.model.entity.StudentSemesterDiscipline;
import com.itmo.db.generator.model.entity.link.PersonGroupLink;
import com.itmo.db.generator.persistence.PersistenceWorkerFactory;
import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.annotations.MergeRepository;
import com.itmo.db.generator.persistence.db.oracle.dao.ItmoObjectOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.dao.ItmoParamOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoAttributeOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoObjectOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoObjectTypeOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoParamOracleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.core.aggregation.Fields;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import javax.persistence.Entity;

@SpringBootApplication
@Slf4j
public class MergeApplication implements ApplicationRunner {

    @Autowired
    PersistenceWorkerFactory persistenceWorkerFactory;
    ItmoAttributeOracleRepository itmoAttributeOracleRepository;
    ItmoObjectOracleRepository itmoObjectOracleRepository;
    ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository;
    ItmoParamOracleRepository itmoParamOracleRepository;

    public MergeApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(MergeApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var result = findAll();
        System.out.println("fetched");
    }

    private Method findSetter(Class clazz, String fieldName) {
        PropertyDescriptor pd;
        try {
            if (log.isTraceEnabled()) {
                log.trace("Trying to get setter for property with name '{}'", fieldName);
            }
            pd = new PropertyDescriptor(fieldName, clazz);
            return pd.getWriteMethod();
        } catch (IntrospectionException | IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private Method findGetter(Class clazz, String fieldName) {
        PropertyDescriptor pd;
        try {
            if (log.isTraceEnabled()) {
                log.trace("Trying to get setter for property with name '{}'", fieldName);
            }
            pd = new PropertyDescriptor(fieldName, clazz);
            return pd.getReadMethod();
        } catch (IntrospectionException | IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private Function getConverter(Class<?> source, Class<?> target) {
        if (target.isAssignableFrom(source)) {
            return (x) -> x;
        }
        if (Character.class.equals(target)) {
            if (String.class.equals(source)) {
                return (Function<String, Character>) x -> x.charAt(0);
            }
        }
        if (Short.class.equals(target)) {
            if (String.class.equals(source)) {
                return (Function<String, Short>) Short::valueOf;
            }
        }
        if (Integer.class.equals(target)) {
            if (String.class.equals(source)) {
                return (Function<String, Integer>) Integer::valueOf;
            }
        }
        if (LocalDate.class.equals(target)) {
            if (CharSequence.class.isAssignableFrom(source)) {
                return (Function<CharSequence, LocalDate>) LocalDate::parse;
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
                            keys.get(0).getReferenceId().intValue(),
                            keys.get(1).getReferenceId().intValue()
                    );
                };
            }
        }
        if (StudentSemesterDiscipline.StudentSemesterDisciplinePK.class.equals(target)) {
            if (Long.class.equals(source)) {
                return (Function<Long, StudentSemesterDiscipline.StudentSemesterDisciplinePK>) (x) -> {
                    var keys = getOracleCompositeKey(x);
                    return new StudentSemesterDiscipline.StudentSemesterDisciplinePK(
                            keys.get(0).getReferenceId().intValue(),
                            keys.get(1).getReferenceId().intValue(),
                            Integer.parseInt(keys.get(2).getValue())
                    );
                };
            }
        }

        log.error("Error during receiving converter from '{}' to '{}'", source.getSimpleName(), target.getSimpleName());
        return null;
    }

    private List<ItmoParamOracleDAO> getOracleCompositeKey(Long keyId) {
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
        } catch (NullPointerException | InvocationTargetException e) {
            log.error("Failed to set arg '{}' to setter '{}' of instance '{}'", arg, setter, instance);
            throw e;
        }
    }


    public HashMap<Class, List> findAll() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Instant start = Instant.now();

        itmoAttributeOracleRepository = persistenceWorkerFactory.getItmoAttributeOracleRepository();
        itmoObjectOracleRepository = persistenceWorkerFactory.getItmoObjectOracleRepository();
        itmoObjectTypeOracleRepository = persistenceWorkerFactory.getItmoObjectTypeOracleRepository();
        itmoParamOracleRepository = persistenceWorkerFactory.getItmoParamOracleRepository();

        // TODO: Build map of Entities and their setters
        // TODO: Build map of Entities to DAOs (+ ItmoEntity)
        // TODO: Build map of DAO(+ ItmoEntity) getter to Entity setter (with possible converter in-between)

        //receive all classes
        var itmoObjectTypeOracleDAOS = itmoObjectTypeOracleRepository.findAll();

        HashMap<Class, List<ItmoObjectOracleDAO>> itmoEntities = new HashMap<>();
        HashMap<Class, List> resultEntities = new HashMap<>();
        HashMap<Class, HashMap<Long, Method>> setters = new HashMap<>();
        HashMap<Method, Method> parsers = new HashMap<>();

        itmoObjectTypeOracleDAOS.forEach(
                itmoObjectTypeOracleDAO -> {
                    try {
                        Class clazz = Class.forName(itmoObjectTypeOracleDAO.getName());
                        List<ItmoObjectOracleDAO> typedEntities = itmoObjectOracleRepository.findByObjectTypeId(itmoObjectTypeOracleDAO.getId());
                        HashMap<Long, Method> classSpecificSetters = new HashMap<>();
                        itmoParamOracleRepository
                                .findByObjectId(typedEntities.get(0).getId())
                                .forEach(itmoParamOracleDAO -> {
                                    String attributeName = itmoAttributeOracleRepository
                                            .findById(itmoParamOracleDAO.getAttributeId())
                                            .get().getName();
                                    Method setter = findSetter(clazz, attributeName);
                                    classSpecificSetters.put(itmoParamOracleDAO.getAttributeId(), setter);
                                    parsers.put(setter, getParser(setter));
                                });
                        setters.put(clazz, classSpecificSetters);
                        itmoEntities.put(clazz, typedEntities);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        );

        AtomicInteger counter = new AtomicInteger();
        for (Class clazz : itmoEntities.keySet()) {
            int total = itmoEntities.get(clazz).size();
            int step = total / 10;
            counter.set(0);
            ArrayList typedEntities = new ArrayList(total);
            HashMap<Long, Method> classSpecificSetters = setters.get(clazz);
            log.info("Starting fetching of entities " + clazz.getName() + " with " + total + " entities total.");
            itmoEntities.get(clazz).forEach(itmoTypedEntity -> {
                try {
                    if (counter.incrementAndGet() % step == 0)
                        log.info("Fetched " + counter + " of " + total + " elements");
                    var resultEntity = clazz.getConstructor().newInstance(null);
                    itmoParamOracleRepository
                            .findByObjectId(itmoTypedEntity.getId())
                            .forEach(itmoParamOracleDAO -> {
                                try {
                                    //log.info("fetching: " + itmoParamOracleDAO);
                                    Method setter = classSpecificSetters.get(itmoParamOracleDAO.getAttributeId());
                                    var arg = itmoParamOracleDAO.getValue() != null ? itmoParamOracleDAO.getValue() : itmoParamOracleDAO.getReferenceId();
                                    try {
                                        setValueWithSpecifiedMethods(resultEntity, setter, arg, parsers.get(setter));
                                    } catch (IllegalArgumentException e) {
                                        log.error("Error invoking value " + arg + " from " + arg.getClass() + " for " + setter.getParameterTypes()[0]);
                                        throw e;
                                    }

                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    log.error("HELP" + e.getMessage());
                                } catch (NullPointerException npe) {
                                    log.error("NPE on entity " + itmoTypedEntity + ". Params: " + itmoParamOracleDAO + "\n" + npe.getMessage());
                                    throw npe;
                                }

                            });
                    // log.info("mergred: " + resultEntity);
                    typedEntities.add(resultEntity);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
            log.info("Finished fetching of entities " + clazz.getName() + " with " + typedEntities.size() + " entities total.");
            resultEntities.put(clazz, typedEntities);
        }

        Instant end = Instant.now();
        log.info("Elapsed: " + Duration.between(start, end));
        return resultEntities;
    }

    public void saveAll() throws Exception {
        HashMap<Class, JpaRepository> mergeRepositoryClassMergeRepositoryMap = new HashMap<>();
        HashMap<Class, JpaRepository> fetchRepositoryClassFetchRepositoryMap = new HashMap<>();

        HashMap<Class, JpaRepository> entityClassMergeRepositoryMap = new HashMap<>();
        HashMap<Class, JpaRepository> daoClassRepositoryMap = new HashMap<>();
        HashMap<Class, Class[]> entityClassDaoListClassMap = new HashMap<>();
        HashMap<Class, HashMap<String, Method>> fieldEntitySetterMap = new HashMap<>();
        HashMap<Class, HashMap<String, Method>> fieldDaoGetterMap = new HashMap<>();
        HashMap<Class, List<Object>> entityClassDaoListMap = new HashMap<>();
        HashMap<Class, List<Field>> entityFields = new HashMap<>();
        HashMap<Method, Method> parsers = new HashMap<>();
        HashMap<Class, List> resultEntities = new HashMap<>();

        Arrays.stream(PersistenceWorkerFactory.class.getFields())
                .filter(field -> field.getClass().isAnnotationPresent(MergeRepository.class))
                .forEach(field -> {
                    try {
                        //find all merge repos and bind instance to class
                        mergeRepositoryClassMergeRepositoryMap.put(field.getClass(), (JpaRepository) findGetter(PersistenceWorkerFactory.class, field.getName()).invoke(persistenceWorkerFactory, null));
                        //find all fetch repos and bind instance to class
                        fetchRepositoryClassFetchRepositoryMap.put(field.getClass(), (JpaRepository) findGetter(PersistenceWorkerFactory.class, field.getName()).invoke(persistenceWorkerFactory, null));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        Class entityClass, daoClass;
        for (BeanDefinition bd : scanner.findCandidateComponents("com.itmo.db.generator.model.entity")) {
            entityClass = Class.forName(bd.getBeanClassName());
            //find all entities and bind merge repository to entity
            entityClassMergeRepositoryMap.put(entityClass, mergeRepositoryClassMergeRepositoryMap.get(((EntityJpaRepository) entityClass.getAnnotation(EntityJpaRepository.class)).clazz()));
            Class[] daos = (((DAO) entityClass.getAnnotation(DAO.class)).clazzes());
            //find all daos for entity and bind all daos to entity
            entityClassDaoListClassMap.put(entityClass, daos);
            //find all fetch repos instances for dao and bind all repos to all daos
            Arrays.stream(daos).forEach(dao ->
                    daoClassRepositoryMap.put(dao, fetchRepositoryClassFetchRepositoryMap.get(((EntityJpaRepository) dao.getAnnotation(EntityJpaRepository.class)).clazz()))
            );
        }

        for (Map.Entry<Class, Class[]> entry : entityClassDaoListClassMap.entrySet()) {
            Class finalEntityClass = entry.getKey();
            //TODO implement logic for multiple daos ....
            Class finalDaoClass = entry.getValue()[0];
            ArrayList<Field> fields = new ArrayList<>();
            HashMap<String, Method> entityClassSpecificSetters = new HashMap<>();
            HashMap<String, Method> daoClassSpecificGetters = new HashMap<>();
            Arrays.stream(finalEntityClass.getFields()).forEach(field -> {
                        //save field for future
                        fields.add(field);
                        //fetch all setters and bind to getters
                        Method setter = findSetter(finalEntityClass, field.getName());
                        entityClassSpecificSetters.put(field.getName(), setter);
                        daoClassSpecificGetters.put(field.getName(), findGetter(finalEntityClass, field.getName()));
                        //bind parsers

                        parsers.put(setter, getParser(setter));
                        //fetch all data from db
                        entityClassDaoListMap.put(
                                entry.getKey(),
                                fetchRepositoryClassFetchRepositoryMap.get(
                                        daoClassRepositoryMap.get(finalDaoClass)
                                ).findAll());
                    }
            );
            fieldDaoGetterMap.put(finalEntityClass, daoClassSpecificGetters);
            fieldEntitySetterMap.put(finalEntityClass, entityClassSpecificSetters);
            entityFields.put(finalEntityClass, fields);
        };

        for (Map.Entry<Class, List<Object>> entry : entityClassDaoListMap.entrySet()) {
            entityClass = entry.getKey();
            ArrayList entities = new ArrayList(entry.getValue().size());
            for (Object daoInstance : entry.getValue()) {
                var resultEntity = entityClass.getConstructor().newInstance(null);
                for (Field field : daoInstance.getClass().getFields()) {
                    Method setter = (Method) fieldDaoGetterMap.get(daoInstance.getClass()).get(field.getName()).invoke(daoInstance, null);
                    setValueWithSpecifiedMethods(resultEntity,
                            setter,
                            fieldEntitySetterMap.get(entityClass).get(field.getName()),
                            parsers.get(setter));

                }
                //todo save one-by-one or batch?
                entityClassMergeRepositoryMap.get(entityClass).save(resultEntity);
                entities.add(resultEntity);
            }
            resultEntities.put(entityClass, entities);
        }

    }
}
