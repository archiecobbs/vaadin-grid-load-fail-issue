
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.example.gui.support.UIUtil;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@SuppressWarnings("serial")
@Configurable(preConstruction = true)
public class ExamplePanel extends VerticalLayout {

    protected final ExampleGrid grid;

    @Autowired
    private ConsoleApplication consoleApplication;

    public ExamplePanel() {

        // Initialize layout
        this.setMargin(false);
        this.setHeight("100%");

        // Build grid
        this.grid = this.buildGrid();

        // Add control bar
        this.add(UIUtil.newControlBar(
          "Press to reload:",
          this.grid.getRefreshButton(),
          this.grid.buildLoadingIndicator(),
          UIUtil.EXPANDER));

        // Add message grid
        this.add(this.grid);
        this.setFlexGrow(100, this.grid);

        // Auto load on first connection
        this.grid.getDataProvider().setReloadOnConnect(true);
    }

    protected ExampleGrid buildGrid() {
        final ExampleGrid examplerid = new ExampleGrid();
        examplerid.setSizeFull();
        return examplerid;
    }
}
