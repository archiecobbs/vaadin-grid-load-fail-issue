
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.ValueProvider;

import java.util.Optional;

/**
 * A "do nothing" {@link ComponentRenderer} used to render values that are already {@link Component}'s.
 *
 * @param <T> the type of the input model object
 */
@SuppressWarnings("serial")
public class SelfRenderer<T> extends ComponentRenderer<Component, T> {

    /**
     * Constructor.
     *
     * @param valueProvider creates a component from a model instance
     * @throws IllegalArgumentException if {@code valueProvider} is null
     */
    public SelfRenderer(final ValueProvider<? super T, ? extends Component> valueProvider) {
        super(new SerializableFunction<T, Component>() {
            @Override
            public Component apply(T obj) {
                return Optional.ofNullable(obj)
                  .<Component>map(valueProvider::apply)
                  .orElseGet(() -> new Text(""));
            }
        });
        if (valueProvider == null)
            throw new IllegalArgumentException("null valueProvider");
    }
}
