
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import org.dellroad.stuff.vaadin24.util.AsyncTaskStatusChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ExampleGrid extends Grid<Integer> implements Connectable {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Button refreshButton = new Button("Search", e -> this.refresh());

    public ExampleGrid() {
        super(Integer.class, false);

        // Initialize
        this.setItems(new ExampleDataProvider());

        // Configure grid
        this.addThemeVariants(GridVariant.LUMO_COMPACT);
        this.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        this.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        this.setWidth("100%");
        this.setSelectionMode(Grid.SelectionMode.NONE);

        // Configure grid
        for (int i = 0; i < 10; i++) {
            final int col = i + 1;
            this.addColumn(row -> String.format("Row %d, Col %d", row, col))
              .setKey(String.format("col%d", col))
              .setFlexGrow(1)
              .setHeader(String.format("Col %d", col))
              .setWidth("100px");
        }

        // Connect data provider when attached, disconnect data provider when detached
        this.connectWhileAttached(this);
    }

    @Override
    public final ExampleDataProvider getDataProvider() {
        return (ExampleDataProvider)super.getDataProvider();
    }

    public Button getRefreshButton() {
        return this.refreshButton;
    }

    public void refresh() {
        this.getDataProvider().reload();
    }

    /**
     * Build a {@link SpinnerBars} component that lights up while this grid is reloading.
     * Make it so that if you click on it, loading is canceled.
     */
    public SpinnerBars buildLoadingIndicator() {
        final SpinnerBars spinner = new SpinnerBars();
        this.getDataProvider().getAsyncTaskManager().addAsyncTaskStatusChangeListener(
          e -> {
            final boolean spin = e.getStatus() == AsyncTaskStatusChangeEvent.STARTED;
            this.log.info("spinner: got {} -> {} spinning", this.nameFor(e), spin ? "start" : "stop");
            spinner.setSpinning(spin);
        });
        spinner.addClickListener(e -> this.getDataProvider().cancel());
        return spinner;
    }

    private String nameFor(AsyncTaskStatusChangeEvent<?> e) {
        switch (e.getStatus()) {
        case AsyncTaskStatusChangeEvent.CANCELED:
            return "CANCELED";
        case AsyncTaskStatusChangeEvent.COMPLETED:
            return "COMPLETED";
        case AsyncTaskStatusChangeEvent.FAILED:
            return "FAILED";
        case AsyncTaskStatusChangeEvent.STARTED:
            return "STARTED";
        default:
            return "???";
        }
    }

// Connectable

    @Override
    public void connect() {
        this.getDataProvider().connect();
    }

    @Override
    public void disconnect() {
        this.getDataProvider().disconnect();
    }
}
