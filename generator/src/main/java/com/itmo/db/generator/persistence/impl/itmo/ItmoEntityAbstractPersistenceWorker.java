package com.itmo.db.generator.persistence.impl.itmo;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.model.entity.NumericallyIdentifiableEntity;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoReference;
import com.itmo.db.generator.persistence.db.oracle.dao.*;
import com.itmo.db.generator.persistence.db.oracle.repository.*;
import lombok.extern.slf4j.Slf4j;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Slf4j
public class ItmoEntityAbstractPersistenceWorker<T extends AbstractEntity<TId>, TId> extends AbstractPersistenceWorker<T, TId> {


    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    protected static ItmoAttributeOracleRepository itmoAttributeOracleRepository;
    protected static ItmoListValueOracleRepository itmoListValueOracleRepository;
    protected static ItmoObjectOracleRepository itmoObjectOracleRepository;
    protected static ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository;
    protected final HashMap<String, Long> itmoIdMap = new HashMap<>();
    protected static ItmoParamOracleRepository itmoParamOracleRepository;

    private static final Object lock = new Object();

    public ItmoEntityAbstractPersistenceWorker(Class<T> entityClass, Generator generator) {
        super(entityClass, generator);

        this.createObjectSpecificProperties(entityClass);
    }

    public static void init(ItmoAttributeOracleRepository itmoAttributeOracleRepository,
                            ItmoListValueOracleRepository itmoListValueOracleRepository,
                            ItmoObjectOracleRepository itmoObjectOracleRepository,
                            ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository,
                            ItmoParamOracleRepository itmoParamOracleRepository) {
        ItmoEntityAbstractPersistenceWorker.itmoAttributeOracleRepository = itmoAttributeOracleRepository;
        ItmoEntityAbstractPersistenceWorker.itmoListValueOracleRepository = itmoListValueOracleRepository;
        ItmoEntityAbstractPersistenceWorker.itmoObjectOracleRepository = itmoObjectOracleRepository;
        ItmoEntityAbstractPersistenceWorker.itmoObjectTypeOracleRepository = itmoObjectTypeOracleRepository;
        ItmoEntityAbstractPersistenceWorker.itmoParamOracleRepository = itmoParamOracleRepository;
    }

    public static void shutdown() {
        executorService.shutdownNow();
    }

    private <V> V executeViaService(Callable<V> runnable) {
        try {
            return executorService.submit(runnable).get();
        } catch (InterruptedException | ExecutionException ex) {
            log.error("Got exception", ex);
            return null;
        }
    }

    public void commit() {
        executeViaService(() -> {
            itmoAttributeOracleRepository.flush();
            itmoListValueOracleRepository.flush();
            itmoObjectOracleRepository.flush();
            itmoObjectTypeOracleRepository.flush();
            itmoParamOracleRepository.flush();
            return null;
        });
    }

    @Override
    protected void doCommit() {
        this.commit();
    }


    public ItmoObjectOracleDAO persist(T entity) {
        return this.persistEntity(entity);
    }

    private <TEntity> ItmoObjectOracleDAO persistEntity(TEntity entity) {
        if (log.isDebugEnabled())
            log.debug("Persisting entity '{}' with name '{}' and objectType '{}' to ORACLE",
                    entity, getObjectName(entity), this.itmoIdMap.get(entity.getClass().getName()));

        ItmoObjectOracleDAO itmoObjectOracleDAO =
                ItmoObjectOracleDAO.builder()
                        .name(getObjectName(entity))
                        .objectTypeId(this.itmoIdMap.get(entity.getClass().getName()))
                        .parentId(null)
                        .build();
        executeViaService(() -> itmoObjectOracleRepository.save(itmoObjectOracleDAO));

        var paramDAOs = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ItmoAttribute.class))
                .map(field -> this.persistField(entity, field, itmoObjectOracleDAO))
                .collect(Collectors.toList());
        executeViaService(() -> itmoParamOracleRepository.saveAll(paramDAOs));

        return itmoObjectOracleDAO;
    }

    private <TEntity> ItmoParamOracleDAO persistField(TEntity entity, Field field, ItmoObjectOracleDAO entityDAO) {
        if (log.isTraceEnabled()) {
            log.trace("Handling field '{}' of entity '{}'", field.getName(), entity);
        }
        ItmoParamOracleDAO itmoParamOracleDAO = ItmoParamOracleDAO.builder()
                .attributeId(this.itmoIdMap.get(getFieldQualifiedName(field)))
                .value(
                        field.getType().isEnum() || this.isReferenceField(field) || this.isCompoundField(field)
                                ? null
                                : String.valueOf(callGetter(entity, field.getName()))
                )
                .listValueId(
                        field.getType().isEnum()
                                ? this.itmoIdMap.get(getFieldQualifiedName(field))
                                : null
                )
                .referenceId(
                        this.isCompoundField(field)
                                ? this.persistEntity(callGetter(entity, field.getName())).getId()
                                : this.isReferenceField(field)
                                ? this.getDependencyDAOId(
                                getReferenceClass(field),
                                (Integer) callGetter(entity, field.getName()),
                                ItmoObjectOracleDAO.class
                        )
                                : null
                )
                .objectId(entityDAO.getId())
                .build();
        if (log.isTraceEnabled()) {
            log.trace("Adding: " + itmoParamOracleDAO.getAttributeId().toString());
        }
        return itmoParamOracleDAO;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(T entity) {
        return List.of(this.persist(entity));
    }

    public void createObjectSpecificProperties(Class<?> entityClass) {
        if (log.isDebugEnabled()) {
            log.debug("Creating entity mapping for oracle schema for entity '{}'", entityClass);
        }

        long generatedObjectTypeId = entityClass.getName().hashCode();
        itmoIdMap.put(entityClass.getName(), generatedObjectTypeId);

        var objectTypeDAO = ItmoObjectTypeOracleDAO.builder()
                .id(generatedObjectTypeId)
                .name(entityClass.getName())
                .description(entityClass.getAnnotation(ItmoEntity.class).description())
                .build();
        executeViaService(() ->
                itmoObjectTypeOracleRepository.save(objectTypeDAO));
        if (log.isTraceEnabled()) {
            log.trace("Generated itmoObjectType: '{}'", objectTypeDAO);
        }

        List<ItmoAttributeOracleDAO> itmoAttributeOracleDAOS = new ArrayList<>();
        List<ItmoListValueOracleDAO> itmoListValueOracleDAOS = new ArrayList<>();
        Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ItmoAttribute.class))
                .forEach(field -> {
                    if (log.isTraceEnabled()) {
                        log.trace("Found field declaration for persistence: '{}'", field.getName());
                    }

                    long generatedAttributeTypeId = getFieldQualifiedName(field).hashCode();
                    itmoIdMap.put(getFieldQualifiedName(field), generatedAttributeTypeId);

                    var attributeDAO = ItmoAttributeOracleDAO.builder()
                            .id(generatedAttributeTypeId)
                            .name(field.getName())
                            .attributeType(field.getType().getName())
                            .build();
                    if (log.isTraceEnabled()) {
                        log.trace("Persisting attribute '{}'", attributeDAO);
                    }
                    synchronized (lock) {
                        itmoAttributeOracleDAOS.add(attributeDAO);
                    }

                    if (field.getType().isAnnotationPresent(ItmoEntity.class)) {
                        if (log.isTraceEnabled()) {
                            log.trace("Field '{}' type is an entity, recursing", field.getName());
                        }
                        this.createObjectSpecificProperties(field.getType());
                        return;
                    }

                    if (!field.getType().isEnum()) {
                        return;
                    }

                    if (log.isTraceEnabled()) {
                        log.trace("Field '{}' is enum, persisting enum values", field.getName());
                    }
                    for (Field enumField : field.getType().getDeclaredFields()) {
                        if (log.isTraceEnabled()) {
                            log.trace("Persisting enum value '{}' for enum '{}'", enumField.getName(), field.getName());
                        }
                        long generatedListValueId = getFieldQualifiedName(field).hashCode();
                        itmoIdMap.put(getFieldQualifiedName(field), generatedListValueId);
                        var itmoListValueDAO = ItmoListValueOracleDAO.builder()
                                .id((long) (getFieldQualifiedName(enumField)).hashCode())
                                .value(enumField.getName())
                                .build();
                        if (log.isTraceEnabled()) {
                            log.trace("Persisting listValue '{}'", itmoListValueDAO);
                        }
                        synchronized (lock) {
                            itmoListValueOracleDAOS.add(itmoListValueDAO);
                        }
                    }
                });
        executeViaService(() -> {
            itmoAttributeOracleRepository.saveAll(itmoAttributeOracleDAOS);
            itmoListValueOracleRepository.saveAll(itmoListValueOracleDAOS);
            return null;
        });
    }

    private static String getFieldQualifiedName(Field field) {
        return String.join(".", field.getType().getName(), field.getName());
    }

    private String getObjectName(Object obj) {
        try {
            if (log.isTraceEnabled()) {
                log.trace("Trying to call method with name '{}'", "getName");
            }
            return truncateName((String) obj.getClass().getDeclaredMethod("getName").invoke(obj));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
            log.warn("Object of type '{}' does not have getName method, retuning String.valueOf()", obj.getClass());
        }
        return truncateName(obj.toString());
    }

    private Object callGetter(Object obj, String fieldName) {
        PropertyDescriptor pd;
        try {
            if (log.isTraceEnabled()) {
                log.trace("Trying to get property with name '{}'", fieldName);
            }
            pd = new PropertyDescriptor(fieldName, obj.getClass());
            return pd.getReadMethod().invoke(obj);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private Method findSetter(Class <? extends NumericallyIdentifiableEntity> clazz, String fieldName) {
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

    private boolean isCompoundField(Field field) {
        return field.getType().isAnnotationPresent(ItmoEntity.class);
    }

    private boolean isReferenceField(Field field) {
        return field.isAnnotationPresent(ItmoReference.class)
                && !field.getAnnotation(ItmoReference.class).isTransient();
    }

    private Class<? extends NumericallyIdentifiableEntity> getReferenceClass(Field field) {
        return field.getDeclaredAnnotation(ItmoReference.class).value();
    }

    private String truncateName(String string) {
        if (string.length() <= 50) {
            return string;
        }
        return string.substring(0, 50);
    }

    public HashMap<Class, List> findAll() throws  NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException{
        var itmoObjectTypeOracleDAOS = itmoObjectTypeOracleRepository.findAll();

        HashMap<Class, List<ItmoObjectOracleDAO>> itmoEntities = new HashMap<>();
        HashMap<Class, List> resultEntities = new HashMap<>();
        HashMap<Class, HashMap<Long, Method>> setters = new HashMap<>();

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
                                    classSpecificSetters.put(itmoParamOracleDAO.getAttributeId(), findSetter(clazz, attributeName));
                                });
                        setters.put(clazz, classSpecificSetters);
                        itmoEntities.put(clazz, typedEntities);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        );

        for (Class clazz : itmoEntities.keySet()){
            List typedEntities = resultEntities.get(clazz);
            HashMap<Long, Method> classSpecificSetters = setters.get(clazz);
            itmoEntities.get(clazz).forEach(itmoTypedEntity -> {
                try {
                    var resultEntity = clazz.getConstructor().newInstance(null);
                    itmoParamOracleRepository
                            .findByObjectId(itmoTypedEntity.getId())
                            .forEach(itmoParamOracleDAO -> {
                                try {
                                    classSpecificSetters.get(itmoParamOracleDAO.getId()).invoke(resultEntity, itmoParamOracleDAO.getValue() != null? itmoParamOracleDAO.getValue() : itmoParamOracleDAO.getReferenceId());
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            
                            });
                    typedEntities.add(resultEntity);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                resultEntities.put(clazz, typedEntities);
            });
        }

        return resultEntities;
    }
}
