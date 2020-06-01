package com.itmo.db.generator.utils.eventbus;

import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.pool.ThreadPoolFactory;
import com.itmo.db.generator.utils.eventmanager.AbstractEventManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Component
public class EventBus extends AbstractEventManager<GeneratorEvent> {

    private static EventBus instance = null;

    private final Map<SenderKey<?, ?>, SenderConsumer<?, ?, ?>> consumerMap;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class SenderKey<T extends AbstractEntity<TId>, TId> implements Serializable {
        private GeneratorEvent eventType;
        private Class<T> sender;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class SenderConsumer<T extends AbstractEntity<TId>, TId, TMessage> implements Serializable {
        private GeneratorEvent eventType;
        private Class<T> sender;
        private List<Consumer<GeneratorEventMessage<T, TId, TMessage>>> consumers;
        private Consumer<GeneratorEventMessage<T, TId, TMessage>> baseConsumer;
    }

    public synchronized static EventBus getInstance() {
        if (instance != null) {
            return instance;
        }

        new EventBus();
        return instance;
    }

    public EventBus() {
        super();
        if (EventBus.instance == null) {
            EventBus.instance = this;
        } else {
            log.error("EventBus instantiated multiple times");
        }
        this.consumerMap = new HashMap<>();
    }

    public <T extends AbstractEntity<TId>, TId, TMessage>
    void subscribe(GeneratorEvent eventType, Consumer<GeneratorEventMessage<T, TId, TMessage>> consumer) {
        super.subscribe(eventType, consumer);
    }

    public <T extends AbstractEntity<TId>, TId, TMessage>
    void subscribe(GeneratorEvent eventType,
                   Class<T> sender,
                   Consumer<GeneratorEventMessage<T, TId, TMessage>> consumer) {
        SenderKey<T, TId> key = new SenderKey<>(eventType, sender);
        synchronized (this.consumerMap) {
            log.info("Adding a subscriber '{}:{}'", eventType, sender);
            if (this.consumerMap.containsKey(key)) {
                log.info("'{}:{}' to existing collection", eventType, sender);
                this.consumerMap.get(key).getConsumers().add((Consumer) consumer);
                return;
            }
            log.info("'{}:{}' to new collection", eventType, sender);
            this.consumerMap.put(key, new SenderConsumer<T, TId, TMessage>(
                            eventType,
                            sender,
                            new ArrayList<>(),
                    (message) -> this.messageHandler(eventType, sender, message, key)
                    )
            );
            log.info("'{}:{}' adding to new collection", eventType, sender);
            this.consumerMap.get(key).getConsumers().add((Consumer) consumer);
            super.subscribe(eventType, this.consumerMap.get(key).getBaseConsumer());
        }
    }

    public <T extends AbstractEntity<TId>, TId, TMessage>
    void unsubscribe(GeneratorEvent eventType, Consumer<GeneratorEventMessage<T, TId, TMessage>> consumer) {
        super.unsubscribe(eventType, consumer);
    }

    public <T extends AbstractEntity<TId>, TId, TMessage>
    void unsubscribe(GeneratorEvent eventType,
                     Class<T> sender,
                     Consumer<GeneratorEventMessage<T, TId, TMessage>> consumer) {
        SenderKey<T, TId> key = new SenderKey<>(eventType, sender);
        synchronized (this.consumerMap) {
            if (!this.consumerMap.containsKey(key)) {
                throw new IllegalArgumentException("No such key exists");
            }
            if (!this.consumerMap.get(key).getConsumers().contains(consumer)) {
                log.warn("Trying to unsubscribe from '{}:{}' but no consumer exists", eventType, sender);
                return;
            }
            this.consumerMap.get(key).getConsumers().remove(consumer);
            log.trace("Removed a subscriber from '{}:{}'", eventType, sender);

            if (!this.consumerMap.get(key).getConsumers().isEmpty()) {
                return;
            }
            log.debug("No more consumers for '{}:{}', removing base subscriber", eventType, sender);
            super.unsubscribe(eventType, this.consumerMap.get(key).getBaseConsumer());
            this.consumerMap.remove(key);
        }
    }

    public <T extends AbstractEntity<TId>, TId, TMessage>
    void notify(GeneratorEvent eventType, GeneratorEventMessage<T, TId, TMessage> arg) {
        super.notify(eventType, arg);
    }

    private <T extends AbstractEntity<TId>, TId, TMessage>
    void messageHandler(GeneratorEvent eventType,
                        Class<T> sender,
                        GeneratorEventMessage<T, TId, TMessage> message,
                        SenderKey<T, TId> key) {
        if (message.getSender() != sender) {
            return;
        }
        log.info("'{}:{}' callback, notifying consumers", eventType, sender);
        synchronized (this.consumerMap) {
            List<Consumer<GeneratorEventMessage<T, TId, TMessage>>> consumers =
                    (List) this.consumerMap.get(key).getConsumers();
            log.info("'{}:{}' Acquired lock, CONSUMERS FOUND: '{}', notifying consumers", eventType, sender, consumers);
            consumers
                    .parallelStream()
                    .map(sub -> (Runnable) () -> sub.accept(message))
                    .forEach(ThreadPoolFactory.getPool()::submit);
        }
    }

}
