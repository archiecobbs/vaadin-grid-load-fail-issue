
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import org.dellroad.stuff.vaadin24.util.AsyncTaskStatusChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ExampleGrid extends Grid<ConfigInfo> implements Connectable {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Button refreshButton = new Button("Search", e -> this.refresh());

    public ExampleGrid() {
        super(ConfigInfo.class, false);

        // Initialize
        this.setItems(new ExampleDataProvider());

        // Configure grid
        this.addThemeVariants(GridVariant.LUMO_COMPACT);
        this.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        this.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        this.setWidth("100%");
        this.setSelectionMode(Grid.SelectionMode.NONE);

        this.addColumn(new SelfRenderer<>(ConfigInfo::propertyRunning))
          .setKey(ConfigInfo.RUNNING_PROPERTY)
          .setHeader("")
          .setTextAlign(ColumnTextAlign.CENTER)
          .setFlexGrow(0)
          .setWidth("20px");

        this.addColumn(new SelfRenderer<>(ConfigInfo::propertyHtmlName))
          .setKey(ConfigInfo.HTML_NAME_PROPERTY)
          .setHeader("Name")
          .setWidth("300px")
          .setResizable(true)
          .setFlexGrow(2);

        this.addColumn(new SelfRenderer<>(ConfigInfo::propertyUUID))
          .setKey(ConfigInfo.UUID_PROPERTY)
          .setHeader("UUID")
          .setTextAlign(ColumnTextAlign.CENTER)
          .setWidth("24em")
          .setFlexGrow(0)
          .setResizable(true)
          .setVisible(false);

        this.addColumn(new SelfRenderer<>(ConfigInfo::propertyStatus))
          .setKey(ConfigInfo.STATUS_PROPERTY)
          .setHeader("Status")
          .setTextAlign(ColumnTextAlign.CENTER)
          .setFlexGrow(0)
          .setWidth("5em");

        this.addColumn(ConfigInfo::propertySignedOff)
          .setKey(ConfigInfo.SIGNED_OFF_PROPERTY)
          .setHeader("Signoff")
          .setTextAlign(ColumnTextAlign.CENTER)
          .setFlexGrow(0)
          .setWidth("6em");

        this.addColumn(new SelfRenderer<>(ConfigInfo::propertyServerName))
          .setKey(ConfigInfo.SERVER_NAME_PROPERTY)
          .setHeader("Server")
          .setResizable(true)
          .setFlexGrow(0)
          .setTextAlign(ColumnTextAlign.CENTER)
          .setWidth("10em");

        this.addColumn(ConfigInfo::propertyTimestamp)
          .setKey(ConfigInfo.TIMESTAMP_PROPERTY)
          .setHeader("Last Modified")
          .setWidth("125px")
          .setTextAlign(ColumnTextAlign.CENTER)
          .setResizable(true)
          .setSortable(true);

        this.addColumn(new SelfRenderer<>(ConfigInfo::propertyUsername))
          .setKey(ConfigInfo.USERNAME_PROPERTY)
          .setHeader("User")
          .setResizable(true)
          .setFlexGrow(0)
          .setTextAlign(ColumnTextAlign.CENTER)
          .setWidth("10em");

        this.addColumn(new SelfRenderer<>(ConfigInfo::propertyComment))
          .setKey(ConfigInfo.COMMENT_PROPERTY)
          .setHeader("Comment")
          .setFlexGrow(3)
          .setWidth("150px");

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
