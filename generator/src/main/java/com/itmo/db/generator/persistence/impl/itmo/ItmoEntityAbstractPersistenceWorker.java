package com.itmo.db.generator.persistence.impl.itmo;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.oracle.annotations.Description;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
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


@Slf4j
public abstract class ItmoEntityAbstractPersistenceWorker<T extends AbstractEntity<TId>, TId> extends AbstractPersistenceWorker<T, TId> {


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
        List<?> itmoObjects = this.doPersist(entity);
        return itmoObjects == null || itmoObjects.isEmpty() ? null : (ItmoObjectOracleDAO) itmoObjects.get(0);
    }


    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(T entity) {
        List<ItmoParamOracleDAO> itmoParamOracleDAOS = new ArrayList<>();
        /// create objects

        log.debug("Persisting entity '{}' with name '{}' and objectType '{}' to ORACLE",
                entity, callGetter(entity, "name"), this.itmoIdMap.get(entity.getClass().getName()));
        ItmoObjectOracleDAO itmoObjectOracleDAO =
                ItmoObjectOracleDAO.builder()
                        .name(String.valueOf(callGetter(entity, "name")))
                        .itmoObjectTypeOracleDAO(
                                new ItmoObjectTypeOracleDAO(
                                        this.itmoIdMap.get(entity.getClass().getName()),
                                        null,
                                        null
                                )
                        )
                        .parent(null)
                        .build();

        this.itmoObjectOracleRepository.save(itmoObjectOracleDAO);

        Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ItmoAttribute.class))
                .forEach(field -> {
                    ItmoParamOracleDAO itmoParamOracleDAO = ItmoParamOracleDAO.builder()
                            .itmoAttributeOracleDAO(
                                    new ItmoAttributeOracleDAO(this.itmoIdMap.get(getFieldQualifiedName(field)), null, null)
                            )
                            .value(field.getType().isAssignableFrom(Enum.class)
                                    ? null
                                    : String.valueOf(callGetter(entity, field.getName()))
                            )
                            .itmoListValueOracleDAO(field.getType().isAssignableFrom(Enum.class)
                                    ? new ItmoListValueOracleDAO(this.itmoIdMap.get(getFieldQualifiedName(field)), null)
                                    : null
                            )
                            .itmoObjectOracleDAO(itmoObjectOracleDAO)
                            .build();
                    itmoParamOracleDAOS.add(itmoParamOracleDAO);
                    log.debug("Adding: " + itmoParamOracleDAO.getItmoAttributeOracleDAO().getId().toString());
                });

        this.itmoParamOracleRepository.saveAll(itmoParamOracleDAOS);
        // INFO: required to return saved object because it contains OBJECT_ID
        //  Then dependent entities will have this id for reference
        return List.of(itmoObjectOracleDAO);
    }


    public void createObjectSpecificProperties(Class<T> entityClass) {
        log.debug("Creating entity mapping for oracle schema for entity '{}'", entityClass);
        ItmoObjectTypeOracleDAO itmoObjectTypeOracleDAO = new ItmoObjectTypeOracleDAO();
        List<ItmoAttributeOracleDAO> itmoAttributeOracleDAOS = new ArrayList<>();
        List<ItmoListValueOracleDAO> itmoListValueOracleDAOS = new ArrayList<>();

        long generatedAttributeTypeId;
        long generatedListValueId;
        long generatedObjectTypeId = entityClass.getName().hashCode();
        itmoIdMap.put(entityClass.getName(), generatedObjectTypeId);
        itmoObjectTypeOracleDAO.setId(generatedObjectTypeId);
        itmoObjectTypeOracleDAO.setName(entityClass.getName());
        itmoObjectTypeOracleDAO.setDescription(entityClass.getAnnotation(Description.class).value());
        itmoObjectTypeOracleRepository.save(itmoObjectTypeOracleDAO);
        log.trace("Generated itmoObjectType: '{}'", itmoObjectTypeOracleDAO);

        for (Field field : entityClass.getDeclaredFields())
            if (field.isAnnotationPresent(ItmoAttribute.class)) {
                log.trace("Found field declaration for persistence: '{}'", field.getName());
                generatedAttributeTypeId = getFieldQualifiedName(field).hashCode();
                itmoIdMap.put(getFieldQualifiedName(field), generatedAttributeTypeId);
                var attributeDAO = ItmoAttributeOracleDAO.builder()
                        .id(generatedAttributeTypeId)
                        .name(field.getName())
                        .attributeType(field.getType().getName())
                        .build();
                log.trace("Persisting attribute '{}'", attributeDAO);
                itmoAttributeOracleDAOS.add(attributeDAO);

                if (field.getType().isAssignableFrom(Enum.class))
                    log.trace("Field '{}' is enum, persisting enum values", field.getName());
                    for (Field enumField : field.getType().getDeclaredFields()) {
                        log.trace("Persisting enum value '{}' for enum '{}'", enumField.getName(), field.getName());
                        generatedListValueId = getFieldQualifiedName(field).hashCode();
                        itmoIdMap.put(getFieldQualifiedName(field), generatedListValueId);
                        var itmoListValueDAO = ItmoListValueOracleDAO.builder()
                                .id((long) (getFieldQualifiedName(enumField)).hashCode())
                                .value(enumField.getName())
                                .build();
                        log.trace("Persisting listValue '{}'", itmoListValueDAO);
                        itmoListValueOracleDAOS.add(itmoListValueDAO);
                    }
            }

        this.itmoObjectTypeOracleRepository.save(itmoObjectTypeOracleDAO);
        this.itmoAttributeOracleRepository.saveAll(itmoAttributeOracleDAOS);
        this.itmoListValueOracleRepository.saveAll(itmoListValueOracleDAOS);
    }

    private static String getFieldQualifiedName(Field field) {
        return String.join(".", field.getType().getName(), field.getName());
    }

    private Object callGetter(Object obj, String fieldName){
        PropertyDescriptor pd;
        try {
            pd = new PropertyDescriptor(fieldName, obj.getClass());
            return pd.getReadMethod().invoke(obj);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void commit() {
        this.doCommit();
    }

}
