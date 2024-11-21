
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.google.common.base.Preconditions;
import com.vaadin.flow.component.AttachNotifier;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachNotifier;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Html;

/**
 * An element containing arbitrary HTML.
 *
 * @see <a href="https://vaadin.com/forum/thread/17072019/inject-an-html-into-a-flow-compoment">Inject an HTML into a Flow Compoment</a>
 */
@SuppressWarnings("serial")
public abstract class AbstractHtml<T extends Component & HasComponents & ClickNotifier<T>> extends Composite<T>
  implements ClickNotifier<T>, HasEnabled, HasStyle, HasSize, AttachNotifier, DetachNotifier {

    private final String tagName;

    private T element;
    private String html;

// Constructor

    protected AbstractHtml(T element, String tagName, String html) {
        Preconditions.checkArgument(element != null, "null element");
        Preconditions.checkArgument(tagName != null, "null tagName");
        this.element = element;
        this.tagName = tagName;
        this.setHtml(html);
    }

// Properties

    public void setHtml(String html) {
        if (html == null)
            html = "";
        if (html.equals(this.html))
            return;
        this.html = html;
        this.element.removeAll();
        this.element.add(new Html("<" + this.tagName + ">" + this.html + "</" + this.tagName + ">"));
    }

    public String getHtml() {
        return this.html;
    }

// Composite

    @Override
    protected T initContent() {
        return this.element;
    }
}
