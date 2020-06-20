package com.itmo.db.generator;

import com.itmo.db.generator.persistence.PersistenceWorkerFactory;
import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.annotations.MergeRepository;
import com.itmo.db.generator.persistence.db.oracle.dao.ItmoObjectOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoAttributeOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoObjectOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoObjectTypeOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoParamOracleRepository;
import com.itmo.db.generator.utils.merge.MergeUtils;
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

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@SpringBootApplication
@Slf4j
public class MergeApplication implements ApplicationRunner {

    @Autowired
    PersistenceWorkerFactory persistenceWorkerFactory;
    @Autowired
    ItmoAttributeOracleRepository itmoAttributeOracleRepository;
    @Autowired
    ItmoObjectOracleRepository itmoObjectOracleRepository;
    @Autowired
    ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository;
    @Autowired
    ItmoParamOracleRepository itmoParamOracleRepository;
    @Autowired
    MergeUtils mergeUtils;

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

    public HashMap<Class, List> findAll() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Instant start = Instant.now();

        // TODO: Build map of Entities and their setters
        // TODO: Build map of Entities to DAOs (+ ItmoEntity)
        // TODO: Build map of DAO(+ ItmoEntity) getter to Entity setter (with possible converter in-between)

        //receive all classes
        var itmoObjectTypeOracleDAOS = itmoObjectTypeOracleRepository.findAll();

        HashMap<Class, List<ItmoObjectOracleDAO>> itmoEntities = new HashMap<>();
        HashMap<Class, List> resultEntities = new HashMap<>();
        HashMap<Class, HashMap<Long, Method>> setters = new HashMap<>();
        HashMap<Method, Function> parsers = new HashMap<>();

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
                                    Method setter = mergeUtils.findSetter(clazz, attributeName);
                                    Method getter = mergeUtils.findGetter(clazz, attributeName);
                                    classSpecificSetters.put(itmoParamOracleDAO.getAttributeId(), setter);
                                    parsers.put(setter, mergeUtils.getConverter(getter.getReturnType(), setter.getParameterTypes()[0]));
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
                                        mergeUtils.setValueWithSpecifiedMethods(resultEntity, setter, arg, parsers.get(setter)); // lol
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
        HashMap<Method, Function> parsers = new HashMap<>();
        HashMap<Class, List> resultEntities = new HashMap<>();

        Arrays.stream(PersistenceWorkerFactory.class.getFields())
                .filter(field -> field.getClass().isAnnotationPresent(MergeRepository.class))
                .forEach(field -> {
                    try {
                        //find all merge repos and bind instance to class
                        mergeRepositoryClassMergeRepositoryMap.put(field.getClass(), (JpaRepository) mergeUtils.findGetter(PersistenceWorkerFactory.class, field.getName()).invoke(persistenceWorkerFactory, null));
                        //find all fetch repos and bind instance to class
                        fetchRepositoryClassFetchRepositoryMap.put(field.getClass(), (JpaRepository) mergeUtils.findGetter(PersistenceWorkerFactory.class, field.getName()).invoke(persistenceWorkerFactory, null));
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
                Method setter = mergeUtils.findSetter(finalEntityClass, field.getName());
                entityClassSpecificSetters.put(field.getName(), setter);
                // ??? vFinal
                Method getter = mergeUtils.findGetter(finalEntityClass, field.getName());
                daoClassSpecificGetters.put(field.getName(), getter);
                //bind parsers
                parsers.put(setter, mergeUtils.getConverter(getter.getReturnType(), setter.getParameterTypes()[0])); // yes
                //fetch all data from db
                entityClassDaoListMap.put(
                        entry.getKey(),
                        fetchRepositoryClassFetchRepositoryMap.get(
                                daoClassRepositoryMap.get(finalDaoClass)
                        ).findAll());
            });
            fieldDaoGetterMap.put(finalEntityClass, daoClassSpecificGetters);
            fieldEntitySetterMap.put(finalEntityClass, entityClassSpecificSetters);
            entityFields.put(finalEntityClass, fields);
        }

        for (Map.Entry<Class, List<Object>> entry : entityClassDaoListMap.entrySet()) {
            entityClass = entry.getKey();
            ArrayList entities = new ArrayList(entry.getValue().size());
            for (Object daoInstance : entry.getValue()) {
                var resultEntity = entityClass.getConstructor().newInstance(null);
                for (Field field : daoInstance.getClass().getFields()) {
                    Method setter = (Method) fieldDaoGetterMap.get(daoInstance.getClass()).get(field.getName()).invoke(daoInstance, null);
                    mergeUtils.setValueWithSpecifiedMethods(resultEntity,
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
