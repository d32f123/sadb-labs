package com.itmo.db.generator.eventmanager;

public interface EventListener<TEventEnum, TCallbackArg> {
    void callback(TEventEnum eventType, TCallbackArg arg);
}
