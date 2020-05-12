package com.itmo.db.generator.utils.eventmanager;

import java.util.function.Consumer;

public interface EventManager<TEventEnum extends Enum<TEventEnum>> {
    <T> void subscribe(TEventEnum eventType, Consumer<T> listener);
    <T> void unsubscribe(TEventEnum eventType, Consumer<T> listener);
    <T> void notify(TEventEnum eventType, T arg);
}
