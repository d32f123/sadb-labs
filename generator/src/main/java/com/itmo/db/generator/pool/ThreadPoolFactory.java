package com.itmo.db.generator.pool;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ThreadPoolFactory {

    private static final int MAX_GENERATOR_THREADS = 25;
    private static final int MAX_PERSISTENCE_WORKER_THREADS = 25;
    private static final int MAX_THREADS = 50;
    private static final Map<ThreadPoolType, ExecutorService> executors = new HashMap<>();

    static {
        executors.put(ThreadPoolType.GENERATOR, Executors.newFixedThreadPool(MAX_GENERATOR_THREADS));
        executors.put(ThreadPoolType.PERSISTENCE_WORKER, Executors.newFixedThreadPool(MAX_PERSISTENCE_WORKER_THREADS));
        executors.put(ThreadPoolType.MISC, Executors.newFixedThreadPool(MAX_THREADS));
    }

    public static ExecutorService getPool(ThreadPoolType threadPoolType) {
        return executors.get(threadPoolType);
    }

    public ExecutorService getPoolInstance(ThreadPoolType threadPoolType) {
        return executors.get(threadPoolType);
    }

    public void shutdown() {
        executors.values().forEach(ExecutorService::shutdownNow);
    }

    public enum ThreadPoolType {
        GENERATOR, PERSISTENCE_WORKER, MISC
    }


}
