package com.itmo.db.generator.utils.eventmanager;

import com.itmo.db.generator.pool.ThreadPoolFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AbstractEventManager<TEventEnum extends Enum<TEventEnum>> implements EventManager<TEventEnum> {
    Map<TEventEnum, List<Consumer<?>>> consumers;
    protected ExecutorService notifier;

    public AbstractEventManager() {
        this.consumers = new ConcurrentHashMap<>();
        this.notifier = Executors.newSingleThreadExecutor();
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

    public synchronized <T> void notify(TEventEnum eventType, T arg) {
        this.consumers.putIfAbsent(eventType, new LinkedList<>());

        List<Consumer<T>> eventConsumers = (List<Consumer<T>>) (List) this.consumers.get(eventType);
        this.notifier.submit(() -> {
            for (Consumer<T> listener : eventConsumers) {
                ThreadPoolFactory.getPool(ThreadPoolFactory.ThreadPoolType.MISC).submit(() -> listener.accept(arg));
            }
        });
    }

    public void shutdown() {
        this.notifier.shutdownNow();
    }
}
