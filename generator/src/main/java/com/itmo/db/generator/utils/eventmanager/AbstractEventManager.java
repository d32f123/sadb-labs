package com.itmo.db.generator.utils.eventmanager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class AbstractEventManager<TEventEnum extends Enum<TEventEnum>> implements EventManager<TEventEnum> {
    Map<TEventEnum, List<Consumer<?>>> consumers;

    public AbstractEventManager() {
        this.consumers = new ConcurrentHashMap<>();
    }

    public <T> void subscribe(TEventEnum eventType, Consumer<T> consumer) {
        this.consumers.putIfAbsent(eventType, new LinkedList<>());

        List<Consumer<?>> eventConsumers = new ArrayList<>(this.consumers.get(eventType));
        eventConsumers.add(consumer);
        this.consumers.put(eventType, eventConsumers);
    }

    public <T> void unsubscribe(TEventEnum eventType, Consumer<T> consumer) {
        this.consumers.putIfAbsent(eventType, new LinkedList<>());

        List<Consumer<?>> eventConsumers = new ArrayList<>(this.consumers.get(eventType));
        eventConsumers.remove(consumer);
        this.consumers.put(eventType, eventConsumers);
    }

    public <T> void notify(TEventEnum eventType, T arg) {
        this.consumers.putIfAbsent(eventType, new LinkedList<>());

        List<Consumer<T>> eventConsumers = (List<Consumer<T>>) (List) this.consumers.get(eventType);
        for (Consumer<T> listener : eventConsumers) {
            new Thread(() -> listener.accept(arg)).start();
        }
    }
}
