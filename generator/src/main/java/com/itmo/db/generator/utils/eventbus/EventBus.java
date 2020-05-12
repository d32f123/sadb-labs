package com.itmo.db.generator.utils.eventbus;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.utils.eventmanager.AbstractEventManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
public class EventBus extends AbstractEventManager<GeneratorEvent> {

    private static EventBus instance = null;

    private final Map<SenderKey, SenderConsumer> consumerMap;

    @Builder
    @Data
    @AllArgsConstructor
    private static class SenderKey implements Serializable {
        private GeneratorEvent eventType;
        private Class<? extends AbstractEntity> sender;
    }

    @Data
    @AllArgsConstructor
    private static class SenderConsumer implements Serializable {
        private GeneratorEvent eventType;
        private Class<? extends AbstractEntity> sender;
        private List<Consumer<GeneratorEventMessage<?>>> consumers;
        private Consumer<GeneratorEventMessage<?>> baseConsumer;
    }

    public synchronized static EventBus getInstance() {
        if (instance != null) {
            return instance;
        }

        instance = new EventBus();
        return instance;
    }

    private EventBus() {
        super();
        this.consumerMap = new HashMap<>();
    }

    public <T> void subscribe(GeneratorEvent eventType, Consumer<GeneratorEventMessage<T>> consumer) {
        super.subscribe(eventType, consumer);
    }

    public <T> void subscribe(GeneratorEvent eventType,
                              Class<? extends AbstractEntity> sender,
                              Consumer<GeneratorEventMessage<T>> consumer) {
        SenderKey key = SenderKey.builder().eventType(eventType).sender(sender).build();
        synchronized (this.consumerMap) {
            log.info("Adding a subscriber '{}:{}'", eventType, sender);
            if (this.consumerMap.containsKey(key)) {
                log.info("'{}:{}' to existing collection", eventType, sender);
                this.consumerMap.get(key).getConsumers().add((Consumer) consumer);
                return;
            }
            log.info("'{}:{}' to new collection", eventType, sender);
            this.consumerMap.put(key, new SenderConsumer(
                            eventType,
                            sender,
                            new ArrayList<>(),
                            (message) -> {
                                if (message.getSender() != sender) {
                                    return;
                                }
                                log.info("'{}:{}' callback, notifying consumers", eventType, sender);
                                synchronized (this.consumerMap) {
                                    List<Consumer<GeneratorEventMessage<?>>> consumers = this.consumerMap.get(key).getConsumers();
                                    consumers.forEach(sub -> new Thread(() -> sub.accept(message)).start());
                                }
                            }
                    )
            );
            log.info("'{}:{}' adding to new collection", eventType, sender);
            this.consumerMap.get(key).getConsumers().add((Consumer) consumer);
            super.subscribe(eventType, this.consumerMap.get(key).getBaseConsumer());
        }
    }

    public <T> void unsubscribe(GeneratorEvent eventType, Consumer<GeneratorEventMessage<T>> consumer) {
        super.unsubscribe(eventType, consumer);
    }

    public <T> void unsubscribe(GeneratorEvent eventType,
                            Class<? extends AbstractEntity> sender,
                            Consumer<GeneratorEventMessage<T>> consumer) {
        SenderKey key = SenderKey.builder().eventType(eventType).sender(sender).build();
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

    public <T> void notify(GeneratorEvent eventType, GeneratorEventMessage<T> arg) {
        super.notify(eventType, arg);
    }
}
