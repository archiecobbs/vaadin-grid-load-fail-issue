
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.example.support.Beans;
import com.google.common.base.Preconditions;
import com.vaadin.flow.server.VaadinSession;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.dellroad.stuff.vaadin24.data.AsyncDataProvider;
import org.dellroad.stuff.vaadin24.util.VaadinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;

@SuppressWarnings("serial")
@Configurable
public class ExampleDataProvider extends AsyncDataProvider<Integer> implements Connectable {

    private static final int NUM_DATA_ROWS = 500;
    private static final long SLEEP_TIME = 1000;        // 1.0 sec

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private boolean reloadOnConnect;

    @Autowired
    @Qualifier(Beans.PRIMARY_TASK_EXECUTOR)
    private AsyncTaskExecutor taskExecutor;

// DataProvider

    @Override
    public Integer getId(Integer item) {
        Preconditions.checkArgument(item != null, "null item");
        return item;
    }

// Public Methods

    public void setReloadOnConnect(boolean reloadOnConnect) {
        this.reloadOnConnect = reloadOnConnect;
    }

    public void reload() {
        VaadinUtil.assertCurrentSession(this.getAsyncTaskManager().getVaadinSession());
        Preconditions.checkState(this.taskExecutor != null, "not connected");
        super.load(this::queryForData);
        this.log.debug("triggered reload, session={}", VaadinSession.getCurrent());
    }

    // This is invoked in a background thread
    private Stream<Integer> queryForData(long id) throws InterruptedException {
        this.log.debug("reload sleeping for {}ms...", SLEEP_TIME);
        Thread.sleep(SLEEP_TIME);       // pretend to be working hard...
        this.log.debug("reload wokeup after {}ms...", SLEEP_TIME);
        final int start = (int)id * NUM_DATA_ROWS;
        return IntStream.range(start, start + NUM_DATA_ROWS).mapToObj(Integer::valueOf);
    }

// AsyncDataProvider overrides

    @Override
    protected void updateFromLoad(long id, final Stream<? extends Integer> stream) {
        VaadinUtil.assertCurrentSession(this.getAsyncTaskManager().getVaadinSession());
        this.log.debug("updating data provider, session={}", VaadinSession.getCurrent());
        super.updateFromLoad(id, stream);
        this.log.debug("done updating data provider...");
    }

    /**
     * This method should not be invoked directly; use {@link #reload} instead.
     *
     * @throws UnsupportedOperationException always
     */
    @Override
    public long load(AsyncDataProvider.Loader<? extends Integer> loader) {
        throw new UnsupportedOperationException();
    }

// DataProvider overrides

    @Override
    public void refreshAll() {
        this.log.debug("refreshAll() invoked");
        super.refreshAll();
    }

// Connectable

    @Override
    public void connect() {
        VaadinUtil.assertCurrentSession(this.getAsyncTaskManager().getVaadinSession());
        this.log.debug("connecting {}", this);
        this.getAsyncTaskManager().setAsyncExecutor(this.taskExecutor::submit);
        if (this.reloadOnConnect)
            this.reload();
    }

    @Override
    public void disconnect() {
        VaadinUtil.assertCurrentSession(this.getAsyncTaskManager().getVaadinSession());
        this.log.debug("disconnecting {}", this);
    }
}
