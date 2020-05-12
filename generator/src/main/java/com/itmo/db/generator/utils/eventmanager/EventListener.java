package com.itmo.db.generator.utils.eventmanager;

public interface EventListener<TEventEnum, TCallbackArg> {
    void callback(TEventEnum eventType, TCallbackArg arg);
}
