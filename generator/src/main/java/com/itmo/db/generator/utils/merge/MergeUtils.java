package com.itmo.db.generator.utils.merge;

import com.itmo.db.generator.model.entity.StudentSemesterDiscipline;
import com.itmo.db.generator.model.entity.link.PersonGroupLink;
import com.itmo.db.generator.persistence.db.oracle.dao.ItmoParamOracleDAO;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoAttributeOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoObjectOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoObjectTypeOracleRepository;
import com.itmo.db.generator.persistence.db.oracle.repository.ItmoParamOracleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

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

    public Method findSetter(Class clazz, String fieldName) {
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

    public Method findGetter(Class clazz, String fieldName) {
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
        } catch (NullPointerException | InvocationTargetException e) {
            log.error("Failed to set arg '{}' to setter '{}' of instance '{}'", arg, setter, instance);
            throw e;
        }
    }

}
