package com.itmo.db.generator.utils.eventbus;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneratorEventMessage<T> {

    private Class<? extends AbstractEntity> sender;
    private T message;

}
