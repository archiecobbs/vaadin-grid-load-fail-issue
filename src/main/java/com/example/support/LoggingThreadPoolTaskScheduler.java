
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.support;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SuppressWarnings("serial")
public class LoggingThreadPoolTaskScheduler extends ThreadPoolTaskScheduler {

    public LoggingThreadPoolTaskScheduler() {
        this.setErrorHandler(t -> this.logger.error("exception in async task: " + t, t));
    }
}
