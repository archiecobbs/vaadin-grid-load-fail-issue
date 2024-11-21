
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.vaadin.flow.component.html.Span;

/**
 * A {@code &lt;span&gt;} element containing arbitrary HTML.
 */
@SuppressWarnings("serial")
public class HtmlSpan extends AbstractHtml<Span> {

    public HtmlSpan() {
        this(null);
    }

    public HtmlSpan(String html) {
        super(new Span(), "span", html);
    }
}
