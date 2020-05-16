package com.itmo.db.generator.utils.eventbus;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneratorEventMessage<T extends AbstractEntity<TId>, TId, TMessage> {

    private Class<T> sender;
    private TMessage message;

}
