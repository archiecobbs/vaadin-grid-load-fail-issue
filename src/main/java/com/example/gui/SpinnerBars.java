
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.example.gui.support.StyleNames;
import com.vaadin.flow.component.html.Div;

@SuppressWarnings("serial")
public class SpinnerBars extends Div {

    private final Div inner = new Div();

    public SpinnerBars() {
        this.inner.add(new Div());
        this.inner.add(new Div());
        this.inner.add(new Div());
        this.inner.add(new Div());
        this.add(inner);
    }

    public void setSpinning(boolean active) {
        if (active) {
            this.inner.addClassName(StyleNames.SPINNER_BARS_INNER);
            this.addClassName(StyleNames.SPINNER_BARS_OUTER);
        } else {
            this.inner.removeClassName(StyleNames.SPINNER_BARS_INNER);
            this.removeClassName(StyleNames.SPINNER_BARS_OUTER);
        }
    }
}
