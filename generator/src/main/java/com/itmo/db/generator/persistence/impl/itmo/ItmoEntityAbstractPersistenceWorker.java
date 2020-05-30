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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class ItmoEntityAbstractPersistenceWorker<T extends AbstractEntity<TId>, TId> extends AbstractPersistenceWorker<T, TId> {


    protected final ItmoAttributeOracleRepository itmoAttributeOracleRepository;
    protected final ItmoListValueOracleRepository itmoListValueOracleRepository;
    protected final ItmoObjectOracleRepository itmoObjectOracleRepository;
    protected final ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository;
    protected final ItmoParamOracleRepository itmoParamOracleRepository;
    protected final HashMap<String, Long> itmoIdMap = new HashMap<>();


    public ItmoEntityAbstractPersistenceWorker(Class<T> entityClass,
                                               Generator generator,
                                               ItmoAttributeOracleRepository itmoAttributeOracleRepository,
                                               ItmoListValueOracleRepository itmoListValueOracleRepository,
                                               ItmoObjectOracleRepository itmoObjectOracleRepository,
                                               ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository,
                                               ItmoParamOracleRepository itmoParamOracleRepository) {
        super(entityClass, generator);
        this.itmoAttributeOracleRepository = itmoAttributeOracleRepository;
        this.itmoListValueOracleRepository = itmoListValueOracleRepository;
        this.itmoObjectOracleRepository = itmoObjectOracleRepository;
        this.itmoObjectTypeOracleRepository = itmoObjectTypeOracleRepository;
        this.itmoParamOracleRepository = itmoParamOracleRepository;
        this.createObjectSpecificProperties(entityClass);
    }

    @Override
    protected void doCommit() {
        this.itmoAttributeOracleRepository.flush();
        this.itmoListValueOracleRepository.flush();
        this.itmoObjectOracleRepository.flush();
        this.itmoObjectTypeOracleRepository.flush();
        this.itmoParamOracleRepository.flush();
    }


    public ItmoObjectOracleDAO persist(T entity) {
        return this.persistEntity(entity);
    }

    private <TEntity> ItmoObjectOracleDAO persistEntity(TEntity entity) {
        log.debug("Persisting entity '{}' with name '{}' and objectType '{}' to ORACLE",
                entity, getObjectName(entity), this.itmoIdMap.get(entity.getClass().getName()));

        ItmoObjectOracleDAO itmoObjectOracleDAO =
                ItmoObjectOracleDAO.builder()
                        .name(getObjectName(entity))
                        .objectTypeId(this.itmoIdMap.get(entity.getClass().getName()))
                        .parentId(null)
                        .build();
        this.itmoObjectOracleRepository.save(itmoObjectOracleDAO);

        var paramDAOs = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ItmoAttribute.class))
                .map(field -> this.persistField(entity, field, itmoObjectOracleDAO))
                .collect(Collectors.toList());
        this.itmoParamOracleRepository.saveAll(paramDAOs);

        return itmoObjectOracleDAO;
    }

    private <TEntity> ItmoParamOracleDAO persistField(TEntity entity, Field field, ItmoObjectOracleDAO entityDAO) {
        log.trace("Handling field '{}' of entity '{}'", field.getName(), entity);
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
        log.trace("Adding: " + itmoParamOracleDAO.getAttributeId().toString());
        return itmoParamOracleDAO;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(T entity) {
        return List.of(this.persist(entity));
    }

    public void createObjectSpecificProperties(Class<?> entityClass) {
        log.debug("Creating entity mapping for oracle schema for entity '{}'", entityClass);

        long generatedObjectTypeId = entityClass.getName().hashCode();
        itmoIdMap.put(entityClass.getName(), generatedObjectTypeId);

        var objectTypeDAO = ItmoObjectTypeOracleDAO.builder()
                .id(generatedObjectTypeId)
                .name(entityClass.getName())
                .description(entityClass.getAnnotation(ItmoEntity.class).description())
                .build();
        itmoObjectTypeOracleRepository.save(objectTypeDAO);
        log.trace("Generated itmoObjectType: '{}'", objectTypeDAO);

        List<ItmoAttributeOracleDAO> itmoAttributeOracleDAOS = new ArrayList<>();
        List<ItmoListValueOracleDAO> itmoListValueOracleDAOS = new ArrayList<>();
        Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ItmoAttribute.class))
                .forEach(field -> {
                    log.trace("Found field declaration for persistence: '{}'", field.getName());

                    long generatedAttributeTypeId = getFieldQualifiedName(field).hashCode();
                    itmoIdMap.put(getFieldQualifiedName(field), generatedAttributeTypeId);

                    var attributeDAO = ItmoAttributeOracleDAO.builder()
                            .id(generatedAttributeTypeId)
                            .name(field.getName())
                            .attributeType(field.getType().getName())
                            .build();
                    log.trace("Persisting attribute '{}'", attributeDAO);
                    itmoAttributeOracleDAOS.add(attributeDAO);

                    if (field.getType().isAnnotationPresent(ItmoEntity.class)) {
                        log.trace("Field '{}' type is an entity, recursing", field.getName());
                        this.createObjectSpecificProperties(field.getType());
                        return;
                    }

                    if (!field.getType().isEnum()) {
                        return;
                    }

                    log.trace("Field '{}' is enum, persisting enum values", field.getName());
                    for (Field enumField : field.getType().getDeclaredFields()) {
                        log.trace("Persisting enum value '{}' for enum '{}'", enumField.getName(), field.getName());
                        long generatedListValueId = getFieldQualifiedName(field).hashCode();
                        itmoIdMap.put(getFieldQualifiedName(field), generatedListValueId);
                        var itmoListValueDAO = ItmoListValueOracleDAO.builder()
                                .id((long) (getFieldQualifiedName(enumField)).hashCode())
                                .value(enumField.getName())
                                .build();
                        log.trace("Persisting listValue '{}'", itmoListValueDAO);
                        itmoListValueOracleDAOS.add(itmoListValueDAO);
                    }
                });

        this.itmoAttributeOracleRepository.saveAll(itmoAttributeOracleDAOS);
        this.itmoListValueOracleRepository.saveAll(itmoListValueOracleDAOS);
    }

    private static String getFieldQualifiedName(Field field) {
        return String.join(".", field.getType().getName(), field.getName());
    }

    private String getObjectName(Object obj) {
        try {
            log.trace("Trying to call method with name '{}'", "getName");
            return truncateName((String) obj.getClass().getDeclaredMethod("getName").invoke(obj));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
            log.warn("Object of type '{}' does not have getName method, retuning String.valueOf()", obj.getClass());
        }
        return truncateName(obj.toString());
    }

    private Object callGetter(Object obj, String fieldName) {
        PropertyDescriptor pd;
        try {
            log.trace("Trying to get property with name '{}'", fieldName);
            pd = new PropertyDescriptor(fieldName, obj.getClass());
            return pd.getReadMethod().invoke(obj);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
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

    public void commit() {
        this.doCommit();
    }

}
