
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.example.gui.support.StyleNames;
import com.google.common.base.Preconditions;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.server.VaadinServletRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.dellroad.stuff.vaadin24.util.VaadinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * Superclass of the various {@link UI}s that constitute the GUI.
 */
@Configurable
@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
@PreserveOnRefresh
@SuppressWarnings("serial")
public abstract class AbstractRoot extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle {

    private static final AtomicInteger ROOT_ID = new AtomicInteger();

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected ConsoleApplication consoleApplication;

    protected boolean loggedIn;

    private final AtomicBoolean initialized = new AtomicBoolean();
    private final VerticalLayout bottomLayout = new VerticalLayout();
    private final Span subtitleLabel = new Span();
    private final int id;

    private UI attachWaitingForCurrentURI;
    private URI currentURI;

    private String rootTitle;

// Constructor

    protected AbstractRoot() {
        this("Window");
    }

    protected AbstractRoot(String rootTitle) {
        VaadinUtil.getCurrentPage().fetchCurrentURL(this::setCurrentURI);       // determine this page's URI ASAP
        this.id = ROOT_ID.incrementAndGet();
        this.setHeight("95%");
        this.setSpacing(false);
        this.setPadding(true);
        this.setMargin(false);
        this.setRootTitle(rootTitle);
        this.addAttachListener(e -> this.handleAttachWhenAble(e.getUI()));
        this.addDetachListener(e -> this.handleDetach());
    }

    private void setCurrentURI(URL url) {
        Preconditions.checkArgument(url != null, "null url");
        try {
            this.currentURI = url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException("unexpected error", e);
        }
        this.consoleApplication.setCurrentURI(this.currentURI);
        if (this.attachWaitingForCurrentURI != null) {
            this.handleAttach(this.attachWaitingForCurrentURI);
            this.attachWaitingForCurrentURI = null;
        }
    }

// BeforeEnterObserver

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    }

// HasDynamicTitle

    @Override
    public String getPageTitle() {
        return this.rootTitle;
    }

// Accessors

    public URI getCurrentURI() {
        Preconditions.checkState(this.currentURI != null, "no currentURI");
        return this.currentURI;
    }

    protected void setRootTitle(String rootTitle) {
        Preconditions.checkArgument(rootTitle != null, "null rootTitle");
        this.rootTitle = rootTitle;
    }

// Lifecycle

    protected void init(VaadinServletRequest request) {
        Preconditions.checkArgument(request != null, "null request");

        // Build layout
        this.buildTopComponents().forEach(this::add);
        this.add(this.bottomLayout);
        this.bottomLayout.setSizeFull();
        this.bottomLayout.setPadding(false);
        this.bottomLayout.setSpacing(false);
    }

    private void handleAttachWhenAble(UI ui) {
        Preconditions.checkArgument(ui != null, "null ui");
        if (this.currentURI == null)
            this.attachWaitingForCurrentURI = ui;
        else
            this.handleAttach(ui);
    }

    // This can be invoked multiple times, e.g., if the user reloads the browser tab.
    // However, we must wait for a non-null "this.currentURI" before we invoke this.
    private void handleAttach(final UI ui) {

        // Logging
        this.log.debug("attaching {}", this);

        // Do one-time initialization
        final VaadinServletRequest request = VaadinServletRequest.getCurrent();
        if (this.initialized.compareAndSet(false, true))
            this.init(request);

        // Update lower panel if needed
        this.updateLowerPanel();
    }

    private void handleDetach() {
        this.log.debug("detaching {}", this);
    }

// User tracking

    protected boolean isApplicationWindow() {
        return true;
    }

// Object

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + "]";
    }

// Subclass hooks

    /**
     * Build the lower panel component.
     *
     * <p>
     * This will only be invoked when we have a logged in user.
     * The returned component may eventually be detached when the user logs out.
     */
    protected abstract Component buildRootLowerPanel();

// Layout construction

    protected List<? extends Component> buildTopComponents() {
        return Arrays.asList(this.buildRootUpperBar(), this.buildRootSeparator());
    }

    private Component buildRootUpperBar() {

        // Logo
        final HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        logoLayout.setWidth(100, Unit.PIXELS);
        //logoLayout.add(logo);

        // Title
        final Span titleLabel = new Span(this.rootTitle);
        titleLabel.addClassName(StyleNames.APPLICATION_TITLE);
        titleLabel.setHeight(24, Unit.PIXELS);
        final VerticalLayout titleLayout = new VerticalLayout();
        titleLayout.setSpacing(false);
        titleLayout.setPadding(false);
        titleLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        titleLayout.setSizeUndefined();
        titleLayout.add(titleLabel);
        titleLayout.add(this.subtitleLabel);
        this.subtitleLabel.addClassName(StyleNames.APPLICATION_SUBTITLE);
        titleLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);

        // Sequence parts
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        layout.addClassName(StyleNames.MAIN_UPPER_BAR);
        layout.add(logoLayout);
        layout.add(titleLayout);
        layout.setFlexGrow(1.0f, titleLayout);
        layout.add(new Button("Logout", e -> {
            this.loggedIn = false;
            VaadinUtil.accessCurrentSession(this::updateLowerPanel);
        }));
        return layout;
    }

    protected Component buildRootSeparator() {
        final Hr separator = new Hr();
        separator.addClassName(StyleNames.MAIN_SEPARATOR);
        separator.setWidth("100%");
        separator.setHeight(3.0f, Unit.PIXELS);
        return separator;
    }

    /**
     * Rebuild and update the lower panel.
     */
    protected void updateLowerPanel() {

        // The user is authorized; build the normal lower panel
        final Component content = this.buildRootLowerPanel();

        // Avoid exception if content is already attached
        content.getParent()
          .filter(HasComponents.class::isInstance)
          .map(HasComponents.class::cast)
          .ifPresent(parent -> parent.remove(content));

        // Update layout
        this.bottomLayout.removeAll();
        this.bottomLayout.add(content);
    }
}
