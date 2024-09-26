
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.google.common.base.Preconditions;
import com.vaadin.flow.component.AttachNotifier;
import com.vaadin.flow.component.DetachNotifier;

/**
 * Implemented by classes that need to be notified when to "connect" and "disconnect".
 */
public interface Connectable {

    /**
     * Connect this instance.
     */
    void connect();

    /**
     * Disconnect this instance.
     */
    void disconnect();

    /**
     * Connect/disconnect this instance whenever the given component is attached/detached (respectively).
     *
     * @param component the component to stay connected with
     * @throws IllegalArgumentException if {@code component} is null
     */
    default <T extends AttachNotifier & DetachNotifier> void connectWhileAttached(T component) {
        Preconditions.checkArgument(component != null, "null component");
        component.addAttachListener(e -> this.connect());
        component.addDetachListener(e -> this.disconnect());
    }
}
