package com.itmo.db.generator.pool;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ThreadPoolFactory {

    private static final int MAX_THREADS = 50;
    private static final ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);

    public static ExecutorService getPool() {
        return pool;
    }

    public ExecutorService getPoolInstance() {
        return pool;
    }


}
