package com.itmo.db.generator;

import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.persistence.PersistenceWorkerFactory;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.*;
import com.itmo.db.generator.persistence.db.oracle.dao.ItmoObjectOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoAttributeOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoObjectOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoObjectTypeOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoParamOracleRepository;
import com.itmo.db.generator.utils.merge.MergeUtils;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class MergeApplication implements ApplicationRunner {

    @Autowired
    PersistenceWorkerFactory persistenceWorkerFactory;
    ItmoAttributeOracleRepository itmoAttributeOracleRepository;
    ItmoObjectOracleRepository itmoObjectOracleRepository;
    ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository;
    ItmoParamOracleRepository itmoParamOracleRepository;
    @Autowired
    MergeUtils mergeUtils;

    private final AtomicInteger counter = new AtomicInteger();
    Map<Class, CrudRepository> mergeRepositoryClassMergeRepositoryMap = new HashMap<>();
    Map<Class, CrudRepository> fetchRepositoryClassFetchRepositoryMap = new HashMap<>();
    Map<Class<? extends AbstractEntity>, EntityMeta> entityMetaMap = new HashMap<>();
    // DAO Class to DAO class ID to EntityId
    Map<Class<? extends IdentifiableDAO>, HashMap<Object, Object>> oldNewObjectsIdMap = new HashMap<>();

    //todo rename methgod..
    public void saveAll() throws Exception {
        this.initializeEntities();

        boolean doBreak = false;
        for (Set<Class> entityClasses : mergeUtils.getEntities()) {
            //TODO parallel execution
            for (Class entityClass : entityClasses.stream().filter(es ->
                    es.isAnnotationPresent(DAO.class)).collect(Collectors.toSet())) {
                var entityMeta = this.entityMetaMap.get(entityClasses);
                for (var daoMeta : entityMeta.daoClasses) {
                    this.saveEntityDAO(entityMeta, daoMeta);
                }
            }
        }
    }

    @Autowired
    public MergeApplication(PersistenceWorkerFactory persistenceWorkerFactory, MergeUtils mergeUtils) {
        itmoAttributeOracleRepository = persistenceWorkerFactory.getItmoAttributeOracleRepository();
        itmoObjectOracleRepository = persistenceWorkerFactory.getItmoObjectOracleRepository();
        itmoObjectTypeOracleRepository = persistenceWorkerFactory.getItmoObjectTypeOracleRepository();
        itmoParamOracleRepository = persistenceWorkerFactory.getItmoParamOracleRepository();
        this.mergeUtils = mergeUtils;
        this.mergeUtils.oldNewObjectsIdMap = this.oldNewObjectsIdMap;
    }

    public static void main(String[] args) {
        SpringApplication.run(MergeApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //var result = findAll();
        saveAll();
        System.out.println("fetched");
    }

    //todo rename methgod..
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

    public void saveEntityDAO(EntityMeta entityMeta, DAOMeta daoMeta) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        var entityClass = entityMeta.entityClass;
        var daoClass = daoMeta.daoClass;
        int total;
        try {
            total = daoMeta.daoInstances.size();
        } catch (NullPointerException e) {
            throw e;
        }

        int step = total / 10;
        HashMap<Object, Object> daoMergeIdMap = new HashMap<>();
        counter.set(0);
        boolean doBreak = false;

        log.info("Starting fetching of entities " + entityClass.getName() + " with " + total + " entities total.");
        for (IdentifiableDAO daoInstance : daoMeta.daoInstances) {
            if (doBreak) {
                doBreak = false;
                break;
            }
            if (counter.incrementAndGet() % step == 0)
                log.info("Fetched " + counter + " of " + total + " elements");

            var entityInstance = entityClass.getConstructor().newInstance();

            if (entityMeta.isLink) {
                entityInstance.setId(daoMeta.converters.get("id").apply(daoInstance.getId()));
            }
            for (Field field : daoInstance.getClass().getDeclaredFields()) {
                var fieldName = field.getName();
                Method setter = entityMeta.setters.get(fieldName);
                Method getter = daoMeta.getters.get(fieldName);
                Function converter = daoMeta.converters.get(fieldName);

                if (field.isAnnotationPresent(FieldSource.class) && !entityMeta.isLink) {
                    Object oldId = getter.invoke(daoInstance);
                    Object newId = oldNewObjectsIdMap.get(field.getAnnotation(FieldSource.class).source()).get(oldId);
                    mergeUtils.setValueWithSpecifiedMethods(
                            entityInstance,
                            setter,
                            newId,
                            converter
                    );
                    continue;
                }
                if (field.isAnnotationPresent(Id.class)) {
                    continue;
                }

                try {
                    mergeUtils.setValueWithSpecifiedMethods(
                            entityInstance,
                            setter,
                            getter.invoke(daoInstance),
                            converter
                    );
                } catch (Exception e) {
                    throw e;
                }

            }

            //todo save one-by-one or batch?
            try {
                entityMeta.mergeRepository.save(entityInstance);
            } catch (Exception XD) {
                throw XD;
            }

            daoMergeIdMap.put(daoInstance.getId(), entityInstance.getId());
            entityMeta.resultInstances.add(entityInstance);
        }
        log.info("Finished fetching of entities " + entityClass.getName() + " with " + total + " entities total.");
        oldNewObjectsIdMap.put(daoClass, daoMergeIdMap);
    }

    public void initializeEntities() throws ClassNotFoundException {
        this.initializeRepositories();

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        for (BeanDefinition bd : scanner.findCandidateComponents("com.itmo.db.generator.model.entity")) {
            Class<AbstractEntity> entityClass = (Class<AbstractEntity>) Class.forName(bd.getBeanClassName());
            List<Field> entityFields = Arrays.asList(entityClass.getDeclaredFields());
            Map<String, Method> entitySetters = new HashMap<>();
            String mergeFieldName = null;

            for (var field : entityFields) {
                entitySetters.put(
                        field.getName(),
                        this.mergeUtils.findSetter(entityClass, field.getName())
                );
                if (field.isAnnotationPresent(MergeKey.class)) {
                    mergeFieldName = field.getName();
                }
            }
            assert mergeFieldName != null;

            var entityMeta = EntityMeta.builder()
                    .entityClass(entityClass)
                    .fields(entityFields.stream().map(Field::getName).collect(Collectors.toUnmodifiableList()))
                    .mergeRepository(
                            this.mergeRepositoryClassMergeRepositoryMap.get(
                                    entityClass.getAnnotation(EntityJpaRepository.class).clazz()
                            )
                    ).setters(entitySetters)
                    .daoClasses(new ArrayList<>())
                    .isLink(
                            entityClass.getName().contains("Link") ||
                                    entityClass.getName().contains("StudentSemesterDiscipline")
                    ).resultInstances(new HashMap<>())
                    .mergeFieldName(mergeFieldName)
                    .build();
            this.entityMetaMap.put(entityClass, entityMeta);

            if (!entityClass.isAnnotationPresent(DAO.class)) {
                continue;
            }

            entityMeta.daoClasses = this.initializeEntityDAO(entityMeta);
        }
    }

    private void initializeRepositories() {
        Arrays.stream(PersistenceWorkerFactory.class.getDeclaredFields())
                .filter(field -> field.getType().isAnnotationPresent(MergeRepository.class))
                .forEach(field -> {
                    try {
                        //find all merge repos and bind instance to class
                        mergeRepositoryClassMergeRepositoryMap.put(field.getType(), (CrudRepository) mergeUtils.findGetter(PersistenceWorkerFactory.class, field.getName()).invoke(persistenceWorkerFactory, null));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
        Arrays.stream(PersistenceWorkerFactory.class.getDeclaredFields())
                .filter(field -> field.getType().isAnnotationPresent(FetchRepository.class))
                .forEach(field -> {
                    try {
                        //find all fetch repos and bind instance to class
                        fetchRepositoryClassFetchRepositoryMap.put(field.getType(), (CrudRepository) mergeUtils.findGetter(PersistenceWorkerFactory.class, field.getName()).invoke(persistenceWorkerFactory, null));
                    } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
                        e.printStackTrace();
                    }
                });
    }

    private List<DAOMeta> initializeEntityDAO(EntityMeta entityMeta) {
        Class<AbstractEntity> entityClass = entityMeta.entityClass;
        Class<? extends IdentifiableDAO>[] daos = entityClass.getAnnotation(DAO.class).clazzes();
        List<DAOMeta> ret = new ArrayList<>();

        for (Class<? extends IdentifiableDAO> daoClass : daos) {
            HashMap<String, Method> getters = new HashMap<>();
            HashMap<String, Function> converters = new HashMap<>();

            entityMeta.fields.forEach(fieldName -> {
                var getter = this.mergeUtils.findGetter(daoClass, fieldName);
                if (getter == null) {
                    return;
                }

                getters.put(fieldName, getter);
                converters.put(fieldName, this.mergeUtils.getConverter(
                        getter.getReturnType(),
                        entityMeta.setters.get(fieldName).getParameterTypes()[0]
                ));
            });

            var daoMeta = DAOMeta.builder()
                    .daoClass(daoClass)
                    .fields(
                            Arrays.stream(daoClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toUnmodifiableList())
                    ).getters(getters)
                    .converters(converters)
                    .daoRepository(
                            fetchRepositoryClassFetchRepositoryMap.get(daoClass.getAnnotation(EntityJpaRepository.class).clazz())
                    )
                    .build();

            List<IdentifiableDAO> daoInstances = new ArrayList<>();
            daoMeta.daoRepository.findAll().forEach(daoInstances::add);
            daoMeta.daoInstances = daoInstances;

            ret.add(daoMeta);
        }

        return ret;
    }

    @Builder
    private static class EntityMeta {
        public Class<AbstractEntity> entityClass;
        public CrudRepository<AbstractEntity, ?> mergeRepository;
        public List<String> fields;
        public Map<String, Method> setters;
        public List<DAOMeta> daoClasses;
        public boolean isLink;
        public String mergeFieldName;

        public Map<Object, AbstractEntity> resultInstances;
    }

    @Builder
    private static class DAOMeta {
        public Class<? extends IdentifiableDAO> daoClass;
        public List<String> fields;
        public Map<String, Method> getters;
        public Map<String, Function> converters;
        public CrudRepository<? extends IdentifiableDAO, ?> daoRepository;

        public List<? extends IdentifiableDAO> daoInstances;
    }
}
