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
import lombok.Data;
import lombok.ToString;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class MergeApplication implements ApplicationRunner {

    @Builder
    @ToString
    private static class EntityMeta {
        public Class<AbstractEntity> entityClass;
        public CrudRepository<AbstractEntity, ?> mergeRepository;
        public List<String> fields;
        public Map<String, Method> getters;
        public Map<String, Method> setters;
        public List<DAOMeta> daoClasses;
        public boolean isLink;
        public Map<Integer, AbstractEntity> resultInstances;
    }

    @Builder
    @ToString
    private static class DAOMeta {
        public Class<? extends IdentifiableDAO> daoClass;
        public List<String> fields;
        public Map<String, Method> getters;
        public Map<String, Function> converters;
        public CrudRepository<? extends IdentifiableDAO, ?> daoRepository;

        public List<? extends IdentifiableDAO> daoInstances;
    }

    @Autowired
    PersistenceWorkerFactory persistenceWorkerFactory;
    ItmoAttributeOracleRepository itmoAttributeOracleRepository;
    ItmoObjectOracleRepository itmoObjectOracleRepository;
    ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository;
    ItmoParamOracleRepository itmoParamOracleRepository;
    @Autowired
    MergeUtils mergeUtils;

    private final AtomicInteger counter = new AtomicInteger();
    Map<Class, CrudRepository> mergeRepositoryClassMergeRepositoryMap = new ConcurrentHashMap<>();
    Map<Class, CrudRepository> fetchRepositoryClassFetchRepositoryMap = new ConcurrentHashMap<>();
    Map<Class<? extends AbstractEntity>, EntityMeta> entityMetaMap = new ConcurrentHashMap<>();
    // DAO Class to DAO class ID to EntityId
    // PersonPostgresDAO -> [daoIds] -> [entityIds]
    Map<Class<? extends IdentifiableDAO>, HashMap<Object, Object>> oldNewObjectsIdMap = new ConcurrentHashMap<>();
    Map<Class<? extends AbstractEntity>, HashMap<Long, Object>> oldNewOracleObjectsIdMap = new ConcurrentHashMap<>();

    final Object generatorStartMonitor = new Object();
    Semaphore generatorSemaphore;

    //todo rename methgod..
    public void saveAll() throws Exception {
        this.initializeEntities();
        fetchEntitiesFromOracle();

        boolean doBreak = false;
        for (Set<Class<? extends AbstractEntity<?>>> entityClasses : mergeUtils.getEntities()) {
            //TODO parallel execution
            this.generatorSemaphore = new Semaphore(entityClasses.size());
            synchronized (this.generatorStartMonitor) {
                if (entityClasses.stream().noneMatch(cls -> cls.isAnnotationPresent(DAO.class))) {
                    continue;
                }
                for (var entityClass : entityClasses) {
                    new Thread(() -> {
                        if (!entityClass.isAnnotationPresent(DAO.class)) {
                            return;
                        }
                        this.generatorSemaphore.acquireUninterruptibly(1);
                        synchronized (this.generatorStartMonitor) {
                            this.generatorStartMonitor.notify();
                        }

                        var entityMeta = this.entityMetaMap.get(entityClass);
                        for (var daoMeta : entityMeta.daoClasses) {
                            try {
                                this.saveEntityDAO(entityMeta, daoMeta);
                            } catch (Exception exception) {
                                log.error("Error during generation of entity '{}', dao '{}'", entityMeta, daoMeta);
                            }
                        }

                        this.generatorSemaphore.release(1);
                    }).start();
                }

                this.generatorStartMonitor.wait();
            }
            this.generatorSemaphore.acquireUninterruptibly(entityClasses.size());
            this.generatorSemaphore.release(entityClasses.size());
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
        this.mergeUtils.oldNewOracleObjectsIdMap = this.oldNewOracleObjectsIdMap;
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
    public void fetchEntitiesFromOracle() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Instant start = Instant.now();

        // TODO: Build map of Entities and their setters
        // TODO: Build map of Entities to DAOs (+ ItmoEntity)
        // TODO: Build map of DAO(+ ItmoEntity) getter to Entity setter (with possible converter in-between)

        //receive all classes
        var itmoObjectTypeOracleDAOS = itmoObjectTypeOracleRepository.findAll();

        HashMap<Class<? extends AbstractEntity>, List<ItmoObjectOracleDAO>> itmoEntities = new HashMap<>();
        HashMap<Class<? extends AbstractEntity>, HashMap<Long, Method>> setters = new HashMap<>();
        HashMap<Method, Function> parsers = new HashMap<>();

        itmoObjectTypeOracleDAOS.forEach(
                itmoObjectTypeOracleDAO -> {
                    try {
                        Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) Class.forName(itmoObjectTypeOracleDAO.getName());
                        List<ItmoObjectOracleDAO> typedEntities = itmoObjectOracleRepository.findByObjectTypeId(itmoObjectTypeOracleDAO.getId());
                        HashMap<Long, Method> classSpecificSetters = new HashMap<>();
                        itmoParamOracleRepository
                                .findByObjectId(typedEntities.get(0).getId())
                                .forEach(itmoParamOracleDAO -> {
                                    String attributeName = itmoAttributeOracleRepository
                                            .findById(itmoParamOracleDAO.getAttributeId())
                                            .get().getName();
                                    Method setter = mergeUtils.findSetter(entityClass, attributeName);
                                    classSpecificSetters.put(itmoParamOracleDAO.getAttributeId(), setter);
                                    parsers.put(setter, mergeUtils.getConverter(itmoParamOracleDAO.getReferenceId() != null ? Long.class : String.class, setter.getParameterTypes()[0]));
                                });
                        setters.put(entityClass, classSpecificSetters);
                        itmoEntities.put(entityClass, typedEntities);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        );

        AtomicInteger counter = new AtomicInteger();
        for (var entityLevel : this.mergeUtils.getEntities()) {
            this.generatorSemaphore = new Semaphore(entityLevel.size());
            if (entityLevel.stream().noneMatch(itmoEntities::containsKey)) {
                continue;
            }
            synchronized (this.generatorStartMonitor) {
                entityLevel.forEach(entityClass -> new Thread(() -> {
                    if (!itmoEntities.containsKey(entityClass)) {
                        return;
                    }
                    this.generatorSemaphore.acquireUninterruptibly(1);
                    synchronized (this.generatorStartMonitor) {
                        this.generatorStartMonitor.notify();
                    }

                    int total = itmoEntities.get(entityClass).size();
                    int step = (total >= 100) ? total / 100 : total;
                    boolean doBreak = false;
                    counter.set(0);

                    this.oldNewOracleObjectsIdMap.put(entityClass, new HashMap<>());
                    EntityMeta entityMeta = entityMetaMap.get(entityClass);
                    ArrayList typedEntities = new ArrayList(total);
                    HashMap<Long, Method> classSpecificSetters = setters.get(entityClass);
                    log.info("Starting fetching of entities " + entityClass.getName() + " with " + total + " entities total.");
                    Instant entityStart = Instant.now();
                    Instant entityEnd = Instant.now();
                    for (ItmoObjectOracleDAO itmoTypedEntity : itmoEntities.get(entityClass)) {
                        try {
                            if (doBreak) {
                                doBreak = false;
                                break;
                            }

                            if (counter.incrementAndGet() % step == 0) {
                                entityEnd = Instant.now();
                                log.info("Fetched {} of {} elements. Elapsed {} seconds.", counter, total, Duration.between(entityStart, entityEnd).toSeconds());
                                entityStart = Instant.now();
                            }
                            AbstractEntity entityInstance = entityClass.getConstructor().newInstance();
                            itmoParamOracleRepository
                                    .findByObjectId(itmoTypedEntity.getId())
                                    .forEach(itmoParamOracleDAO -> {
                                        try {
                                            //log.info("fetching: " + itmoParamOracleDAO);
                                            Method setter = classSpecificSetters.get(itmoParamOracleDAO.getAttributeId());
                                            var isReference = itmoParamOracleDAO.getReferenceId() != null;
                                            Function parser = parsers.get(setter);
                                            Object arg = !isReference
                                                    ? itmoParamOracleDAO.getValue()
                                                    : itmoParamOracleDAO.getReferenceId();
                                            try {
                                                mergeUtils.setValueWithSpecifiedMethods(
                                                        entityInstance,
                                                        setter,
                                                        arg,
                                                        parser
                                                ); // lol
                                            } catch (Exception e) {
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
                            entityMeta.mergeRepository.save(entityInstance);

                            // K P A C U B O
                            this.oldNewOracleObjectsIdMap.get(entityClass).put(itmoTypedEntity.getId(), entityInstance.getId());
                            entityMeta.resultInstances.put(entityInstance.getMergeKey(), entityInstance);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    log.info("Finished fetching and saving of entities " + entityClass.getName() + " with " + typedEntities.size() + " entities total.");
                    this.generatorSemaphore.release(1);
                }).start());

                try {
                    this.generatorStartMonitor.wait();
                } catch (InterruptedException e) {
                    log.error("heheh", e);
                }
            }
            this.generatorSemaphore.acquireUninterruptibly(entityLevel.size());
            this.generatorSemaphore.release(entityLevel.size());
        }


        Instant end = Instant.now();
        log.info("Elapsed: " + Duration.between(start, end));
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
            var entityInstance = this.convertDAOToEntity(entityMeta, daoMeta, daoInstance);

            var mergeKey = entityInstance.getMergeKey();
            if (entityMeta.resultInstances.containsKey(mergeKey)) {
                // Merge into existing instance
                AbstractEntity existingEntity = entityMeta.resultInstances.get(mergeKey);
                this.mergeFields(daoMeta, entityMeta, entityInstance, existingEntity);
                entityInstance = existingEntity;
            }

            //todo save one-by-one or batch?
            try {
                entityMeta.mergeRepository.save(entityInstance);
            } catch (Exception XD) {
                throw XD;
            }

            daoMergeIdMap.put(daoInstance.getId(), entityInstance.getId());
            entityMeta.resultInstances.put(entityInstance.getMergeKey(), entityInstance);
        }
        log.info("Finished fetching of entities " + entityClass.getName() + " with " + total + " entities total.");
        oldNewObjectsIdMap.put(daoClass, daoMergeIdMap);
    }

    public AbstractEntity convertDAOToEntity(EntityMeta entityMeta, DAOMeta daoMeta, IdentifiableDAO daoInstance)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        var entityClass = entityMeta.entityClass;

        AbstractEntity entityInstance = entityClass.getConstructor().newInstance();

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
                        (x) -> x
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
        return entityInstance;
    }

    public void mergeFields(DAOMeta daoMeta, EntityMeta entityMeta, AbstractEntity newEntity, AbstractEntity oldEntity) {
        daoMeta.fields.forEach(fieldName -> {
            if (fieldName.equals("id")) {
                return;
            }
            try {
                this.mergeUtils.setValueWithSpecifiedMethods(
                        oldEntity,
                        entityMeta.setters.get(fieldName),
                        entityMeta.getters.get(fieldName).invoke(newEntity),
                        (x) -> x
                );
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
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

    public void initializeEntities() throws ClassNotFoundException {
        this.initializeRepositories();

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        for (BeanDefinition bd : scanner.findCandidateComponents("com.itmo.db.generator.model.entity")) {
            Class<AbstractEntity> entityClass = (Class<AbstractEntity>) Class.forName(bd.getBeanClassName());
            List<Field> entityFields = Arrays.asList(entityClass.getDeclaredFields());
            Map<String, Method> entitySetters = new HashMap<>();
            Map<String, Method> entityGetters = new HashMap<>();
            String mergeFieldName = null;

            for (var field : entityFields) {
                entitySetters.put(
                        field.getName(),
                        this.mergeUtils.findSetter(entityClass, field.getName())
                );
                entityGetters.put(
                        field.getName(),
                        this.mergeUtils.findGetter(entityClass, field.getName())
                );
            }

            var entityMeta = EntityMeta.builder()
                    .entityClass(entityClass)
                    .fields(entityFields.stream().map(Field::getName).collect(Collectors.toUnmodifiableList()))
                    .mergeRepository(
                            this.mergeRepositoryClassMergeRepositoryMap.get(
                                    entityClass.getAnnotation(EntityJpaRepository.class).clazz()
                            )
                    ).setters(entitySetters)
                    .getters(entityGetters)
                    .daoClasses(new ArrayList<>())
                    .isLink(
                            entityClass.getName().contains("Link") ||
                                    entityClass.getName().contains("StudentSemesterDiscipline")
                    ).resultInstances(new HashMap<Integer, AbstractEntity>())
                    .build();
            this.entityMetaMap.put(entityClass, entityMeta);

            if (!entityClass.isAnnotationPresent(DAO.class)) {
                continue;
            }

            entityMeta.daoClasses = this.initializeEntityDAO(entityMeta);
        }
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

}
