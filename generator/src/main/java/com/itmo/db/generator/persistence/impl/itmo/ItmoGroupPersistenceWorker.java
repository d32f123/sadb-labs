package com.itmo.db.generator.persistence.impl.itmo;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.Group;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.oracle.dao.*;
import com.itmo.db.generator.persistence.db.oracle.repository.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ItmoGroupPersistenceWorker extends ItmoEntityAbstractPersistenceWorker<Group, Integer> {

    public ItmoGroupPersistenceWorker(Class<Group> entityClass, Generator generator, ItmoAttributeOracleRepository itmoAttributeOracleRepository, ItmoListValueOracleRepository itmoListValueOracleRepository, ItmoObjectOracleRepository itmoObjectOracleRepository, ItmoObjectTypeOracleRepository itmoObjectTypeOracleRepository, ItmoParamOracleRepository itmoParamOracleRepository) {
        super(entityClass, generator, itmoAttributeOracleRepository, itmoListValueOracleRepository, itmoObjectOracleRepository, itmoObjectTypeOracleRepository, itmoParamOracleRepository);
    }

}
