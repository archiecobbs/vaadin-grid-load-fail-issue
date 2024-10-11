
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("")
@SuppressWarnings("serial")
public class MainRoot extends AbstractRoot {

    public static final String URI_PATH = "main";
    public static final String TITLE = "Main";

    public MainRoot() {
        super(TITLE);
    }

    @Override
    protected Component buildRootLowerPanel() {

        // Initialize layout
        final VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setSizeFull();

        // Add example
        if (!this.loggedIn) {
            layout.add(new Button("Login", e -> {
                this.loggedIn = true;
                final VaadinSession session = VaadinSession.getCurrent();

                // Ensure there's no UI.getCurrent() when updateLowerPanel() runs...
                new Thread(() -> session.accessSynchronously(this::updateLowerPanel)).start();
            }));
        } else
            layout.add(new ExamplePanel());

        // Done
        return layout;
    }
}
