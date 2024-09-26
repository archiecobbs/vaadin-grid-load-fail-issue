
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.support;

import com.google.common.base.Preconditions;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SuppressWarnings("serial")
public class LoggingThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    public LoggingThreadPoolTaskExecutor() {
        this.setTaskDecorator(this::wrap);
    }

    // These methods work around https://github.com/spring-projects/spring-framework/issues/25231

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(this.wrap(task));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(this.wrap(task));
    }

    private Runnable wrap(Runnable action) {
        Preconditions.checkArgument(action != null, "null action");
        return () -> {
            try {
                action.run();
            } catch (Throwable t) {
                this.logger.error("exception in async task: " + t, t);
                throw t;
            }
        };
    }

    private <T> Callable<T> wrap(Callable<T> action) {
        Preconditions.checkArgument(action != null, "null action");
        return () -> {
            try {
                return action.call();
            } catch (Throwable t) {
                this.logger.error("exception in async task: " + t, t);
                throw t;
            }
        };
    }
}
