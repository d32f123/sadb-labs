package com.itmo.db.generator.persistence;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityPersistedEventMessage<TEntityId> {

    public TEntityId entityId;
    public Map<Class<? extends IdentifiableDAO<?>>, ?> daoValuesMap;
}
