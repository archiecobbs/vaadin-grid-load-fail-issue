
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("other")
@SuppressWarnings("serial")
public class OtherRoot extends AbstractRoot {

    public static final String TITLE = "Other";

    private static OtherRoot firstRoot;
    private static volatile boolean firstRootRunning;

    private final VaadinSession session = VaadinSession.getCurrent();

    private final Span span = new Span();
    private final Button button = new Button("Press Me", e -> this.buttonPressed());
    private final boolean first;

    private UI ui;

    public OtherRoot() {
        super(TITLE);

        synchronized (OtherRoot.class) {
            if ((this.first = firstRoot == null))
                firstRoot = this;
        }
        this.log.info("Created {}, first={}", this, this.first);
    }

    @Override
    protected Component buildRootLowerPanel() {

        // Initialize layout
        final VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setSizeFull();

        // Add example
        this.ui = UI.getCurrent();
        layout.add(new Span("This is " + (this.first ? "" : " not") + " the first root"));
        layout.add(new Span("My session is " + this.session));
        layout.add(new Span("My actual UI is " + this.ui));
        layout.add(this.button);
        layout.add(this.span);

        // Done
        return layout;
    }

    private void buttonPressed() {
        this.button.setEnabled(false);
        if (this.first)
            this.firstButtonPressed();
        else
            this.secondButtonPressed();
    }

    private void firstButtonPressed() {
    this.log.info("{} first root button pressed", this);
        new Thread(this::firstRootLoop).start();
    }

    private void firstRootLoop() {
        this.log.info("{} starting periodic UI synchronous access", this);
        for (int i = 0; i < 10; i++) {      // run for about 10 seconds
            this.log.info("{} sleeping before grabbing lock", this);
            this.sleep(1000);
            this.log.info("{} grabbing the lock", this);
            this.ui.accessSynchronously(() -> {
                firstRootRunning = true;
                try {
                    this.log.info("{} lock grabbed, now sleeping...", this);
                    this.sleep(100);
                } finally {
                    firstRootRunning = false;
                    this.log.info("{} wokeup", this);
                }
            });
        }
        this.session.access(() -> this.button.setEnabled(true));
        this.log.info("{} firstRootLoop exiting", this);
    }

    private void secondButtonPressed() {
    this.log.info("{} non-first root button pressed", this);
        new Thread(this::secondRootLoop).start();
    }

    private void secondRootLoop() {
        this.log.info("{} waiting for first root to grab the lock", this);
        for (int i = 0; i < 200; i++) {     // run for about 10 seconds
            if (!firstRootRunning) {
                sleep(50);
                continue;
            }
            this.log.info("{} enqueuing my session action", this);
            this.session.access(() -> this.span.setText("My current UI is " + UI.getCurrent()));
            break;
        }
        this.session.access(() -> this.button.setEnabled(true));
        this.log.info("{} secondRootLoop exiting", this);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            return;
        }
    }
}
