package com.itmo.db.generator;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.*;
import com.itmo.db.generator.model.entity.link.*;
import com.itmo.db.generator.persistence.PersistenceWorkerFactory;
import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.annotations.MergeRepository;
import com.itmo.db.generator.persistence.db.oracle.dao.ItmoObjectOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.dao.ItmoParamOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.repository.*;
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
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class MergeApplication implements ApplicationRunner {

    protected final Generator generator;
    @Autowired
    PersistenceWorkerFactory persistenceWorkerFactory;
    ItmoAttributeOracleRepository itmoAttributeOracleRepository;
    ItmoObjectOracleRepository itmoObjectOracleRepository;
    ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository;
    ItmoParamOracleRepository itmoParamOracleRepository;

    public MergeApplication(@Autowired Generator generator) {
        this.generator = generator;
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

    private Method getParser(Method method) {
        try {
            if ((method.getParameterTypes()[0]).equals(Character.class))
                // return Character.class.getMethod("valueOf", String.class);
                return null;
            if ((method.getParameterTypes()[0]).equals(Short.class))
                return Short.class.getMethod("valueOf", String.class);
            if ((method.getParameterTypes()[0]).equals(Integer.class))
                return Integer.class.getMethod("valueOf", String.class);
            else if ((method.getParameterTypes()[0]).equals(LocalDate.class))
                return LocalDate.class.getMethod("parse", CharSequence.class);
            else if ((method.getParameterTypes()[0]).equals(boolean.class))
                return Boolean.class.getMethod("getBoolean", String.class);
            else if ((method.getParameterTypes()[0]).equals(LocalTime.class))
                return LocalTime.class.getMethod("parse", CharSequence.class);
//        else
//            method.invoke(resultEntity, arg);
        } catch (NoSuchMethodException e) {
            log.error("Error during receiving parser");
        }
        return null;
    }

    public void setValueWithSpecifiedMethods(Object instance, Object arg, Method method, Method parser) throws InvocationTargetException, IllegalAccessException {
        try {
            if (arg == null || arg.equals("null"))
                return;
            if (parser != null)
                method.invoke(instance, parser.invoke(parser.getReturnType(), arg.toString()));
            else if (Character.class.equals(method.getParameterTypes()[0]))
                method.invoke(instance, arg.toString().charAt(0));
            else if (PersonGroupLink.PersonGroupLinkPK.class.equals(method.getParameterTypes()[0])) {
                List<ItmoParamOracleDAO> personGroupLinkPK = itmoParamOracleRepository.findByObjectId((Long) arg);
                method.invoke(instance, new PersonGroupLink.PersonGroupLinkPK(personGroupLinkPK.get(0).getReferenceId().intValue(), personGroupLinkPK.get(1).getReferenceId().intValue()));
            } else if (StudentSemesterDiscipline.StudentSemesterDisciplinePK.class.equals(method.getParameterTypes()[0])) {
                List<ItmoParamOracleDAO> personGroupLinkPK = itmoParamOracleRepository.findByObjectId((Long) arg);
                personGroupLinkPK.sort((o1, o2) -> (int) ((o1.getId() - o2.getId())));
                try {
                    method.invoke(instance, new StudentSemesterDiscipline.StudentSemesterDisciplinePK(
                            personGroupLinkPK.get(0).getReferenceId().intValue(),
                            personGroupLinkPK.get(1).getReferenceId().intValue(),
                            Integer.parseInt(personGroupLinkPK.get(2).getValue())));
                } catch (NullPointerException e) {
                    throw e;
                }
            } else
                method.invoke(instance, arg);

        } catch (NullPointerException | InvocationTargetException e) {
            throw e;
        }
    }


    public HashMap<Class, List> findAll() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Instant start = Instant.now();

        itmoAttributeOracleRepository = persistenceWorkerFactory.getItmoAttributeOracleRepository();
        itmoObjectOracleRepository = persistenceWorkerFactory.getItmoObjectOracleRepository();
        itmoObjectTypeOracleRepository = persistenceWorkerFactory.getItmoObjectTypeOracleRepository();
        itmoParamOracleRepository = persistenceWorkerFactory.getItmoParamOracleRepository();

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
                                    Method method = classSpecificSetters.get(itmoParamOracleDAO.getAttributeId());
                                    var arg = itmoParamOracleDAO.getValue() != null ? itmoParamOracleDAO.getValue() : itmoParamOracleDAO.getReferenceId();
                                    try {
                                        setValueWithSpecifiedMethods(resultEntity, arg, method, parsers.get(method));
                                    } catch (IllegalArgumentException e) {
                                        log.error("Error invoking value " + arg + " from " + arg.getClass() + " for " + method.getParameterTypes()[0]);
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

    public void saveAll() throws ClassNotFoundException {
        HashMap<Class, JpaRepository> mergeRepositoryClassMergeRepositoryMap = new HashMap<>();
        HashMap<Class, JpaRepository> entityClassMergeRepositoryMap = new HashMap<>();
        HashMap<Class, JpaRepository> daoClassRepositoryMap = new HashMap<>();
        HashMap<Class, List> entityClassDaoListMap = new HashMap<>();

        Arrays.stream(PersistenceWorkerFactory.class.getFields())
                .filter(field -> field.getClass().isAnnotationPresent(MergeRepository.class))
                .forEach(field -> {
                    try {
                        mergeRepositoryClassMergeRepositoryMap.put(field.getClass() , (JpaRepository)findGetter(PersistenceWorkerFactory.class, field.getName()).invoke(persistenceWorkerFactory, null));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        Class clazz;
        for (BeanDefinition bd : scanner.findCandidateComponents("com.itmo.db.generator.model.entity")) {
            clazz = Class.forName(bd.getBeanClassName());
            entityClassMergeRepositoryMap.put(clazz, mergeRepositoryClassMergeRepositoryMap.get(((EntityJpaRepository)clazz.getAnnotation(EntityJpaRepository.class)).clazz()));
            Arrays.stream((((DAO) clazz.getAnnotation(DAO.class)).clazzes())).forEach( dao->
                    //TODO: receive all repostieries of daos
                    ///daoClassRepositoryMap.put(dao, dao.getAnnotation())
            );
        }




    }
}
