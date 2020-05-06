package com.itmo.db.generator.eventmanager;

public interface IEventManager<TEventEnum extends Enum<TEventEnum>, TCallbackArg> {
    void subscribe(TEventEnum eventType, EventListener<TEventEnum, TCallbackArg> listener);
    void unsubscribe(TEventEnum eventType, EventListener<TEventEnum, TCallbackArg> listener);
    void notify(TEventEnum eventType, TCallbackArg arg);
}
