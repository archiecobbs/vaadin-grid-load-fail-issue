
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.example.support.Beans;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.scopes.VaadinSessionScope;

import java.net.URI;

import org.dellroad.stuff.vaadin24.util.VaadinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
@Scope(VaadinSessionScope.VAADIN_SESSION_SCOPE_NAME)
@SuppressWarnings("serial")
public class ConsoleApplication implements ErrorHandler, InitializingBean {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final VaadinSession session = VaadinUtil.getCurrentSession();

    @Autowired
    @Qualifier(Beans.PRIMARY_TASK_SCHEDULER)
    private TaskScheduler primaryTaskScheduler;

    @Autowired
    @Qualifier(Beans.SECONDARY_TASK_SCHEDULER)
    private TaskScheduler secondaryTaskScheduler;

// InitializingBean

    @Override
    public void afterPropertiesSet() {
    }

// Base URI

    public void setCurrentURI(URI uri) {
        this.log.info("determined URI {}", uri);
    }

// Single Sign-On State

    public Logger getLog() {
        return this.log;
    }

// Accessors

    public static ConsoleApplication get() {
        VaadinUtil.assertCurrentSession();
        @Configurable
        class Accessor {
            @Autowired
            ConsoleApplication consoleApplication;
        }
        return new Accessor().consoleApplication;
    }

// Other Beans

    public TaskScheduler getPrimaryTaskScheduler() {
        return this.primaryTaskScheduler;
    }

    public TaskScheduler getSecondaryTaskScheduler() {
        return this.secondaryTaskScheduler;
    }

// ErrorHandler

    @Override
    public void error(ErrorEvent event) {
        final Throwable e = event.getThrowable();
        this.log.error("Vaadin application error: {}", e, e);
    }
}
