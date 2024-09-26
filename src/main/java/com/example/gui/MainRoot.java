
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

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
        final ExamplePanel panel = new ExamplePanel();
        layout.add(panel);

        // Done
        return layout;
    }
}
