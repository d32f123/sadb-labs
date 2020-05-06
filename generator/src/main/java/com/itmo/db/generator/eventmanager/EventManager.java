package com.itmo.db.generator.eventmanager;

import java.util.*;

public class EventManager<TEventEnum extends Enum<TEventEnum>, TCallbackArg>
        implements IEventManager<TEventEnum, TCallbackArg> {
    Map<TEventEnum, List<EventListener<TEventEnum, TCallbackArg>>> eventsListeners;

    public <T extends Class<TEventEnum>> EventManager() {
        this.eventsListeners = new HashMap<>();
    }

    public void subscribe(TEventEnum eventType, EventListener<TEventEnum, TCallbackArg> listener) {
        this.eventsListeners.putIfAbsent(eventType, new LinkedList<>());

        List<EventListener<TEventEnum, TCallbackArg>> eventListeners = eventsListeners.get(eventType);
        eventListeners.add(listener);
    }

    public void unsubscribe(TEventEnum eventType, EventListener<TEventEnum, TCallbackArg> listener) {
        this.eventsListeners.putIfAbsent(eventType, new LinkedList<>());

        List<EventListener<TEventEnum, TCallbackArg>> eventListeners = this.eventsListeners.get(eventType);
        eventListeners.remove(listener);
    }

    public void notify(TEventEnum eventType, TCallbackArg arg) {
        this.eventsListeners.putIfAbsent(eventType, new LinkedList<>());

        List<EventListener<TEventEnum, TCallbackArg>> eventListeners = this.eventsListeners.get(eventType);
        for (EventListener<TEventEnum, TCallbackArg> listener : eventListeners) {
            listener.callback(eventType, arg);
        }
    }
}
