
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui.support;

import com.google.common.base.Preconditions;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public final class UIUtil {

    public static final Object EXPANDER = new Object();
    public static final Object NO_LABEL_MERGE = new Object();

    private UIUtil() {
    }

    /**
     * Build a horizontal bar of labeled fields, where the labels go on top of the fields.
     *
     * @param items one or more {@link Components}, each optionally prefixed with a {@link String} label
     */
    public static HorizontalLayout newControlBar(Object... items) {

        // Sanity check
        Preconditions.checkArgument(items != null, "null items");

        // Initialize layout
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        layout.setMargin(false);

        // Add components, using FormLayout for labels
        String label = null;
        for (Object item : items) {
            if (item == null)
                label = null;
            else if (item == NO_LABEL_MERGE) {
                if (label != null) {
                    final Span span = new Span(label);
                    layout.add(span);
                    layout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, span);
                }
                label = null;
            } else if (item == EXPANDER) {
                Preconditions.checkArgument(label == null, "invalid EXPANDER label");
                final Span expander = new Span();
                layout.setFlexGrow(100, expander);
                layout.add(expander);
                layout.setWidth(100, Unit.PERCENTAGE);
            } else if (item instanceof String) {
                Preconditions.checkArgument(label == null, "duplicate label");
                label = (String)item;
            } else if (item instanceof Component) {
                Component component = (Component)item;
                if (label != null) {
                    if (item instanceof ComboBox)
                        ((ComboBox<?>)item).setLabel(label);
                    else if (item instanceof TextField)
                        ((TextField)item).setLabel(label);
                    else if (item instanceof DatePicker)
                        ((DatePicker)item).setLabel(label);
                    else if (item instanceof DateTimePicker)
                        ((DateTimePicker)item).setLabel(label);
                    else {
                        final FormLayout formLayout = UIUtil.newFormLayout(1, true);
                        formLayout.addFormItem(component, label);
                        component = formLayout;
                    }
                    label = null;
                }
                layout.add(component);
            } else
                throw new IllegalArgumentException("neither Component nor String: " + item.getClass().getName());
        }

        // Done
        return layout;
    }

    /**
     * Create a {@link FormLayout} with a fixed number of columns label positions to the side.
     *
     * @param formLayout the {@link FormLayout} to configure
     * @param numColumns number of columns
     * @return new {@link FormLayout}
     */
    public static FormLayout newFormLayout(int numColumns) {
        return UIUtil.newFormLayout(numColumns, false);
    }

    /**
     * Create a {@link FormLayout} with a fixed number of columns and specified label positions.
     *
     * @param numColumns number of columns
     * @param labelsOnTop true for labels on top, false for labels to the side
     * @return new {@link FormLayout}
     */
    public static FormLayout newFormLayout(int numColumns, boolean labelsOnTop) {
        Preconditions.checkArgument(numColumns > 0, "invalid numColumns");
        final FormLayout formLayout = new FormLayout();
        final FormLayout.ResponsiveStep.LabelsPosition labelsPosition = labelsOnTop ?
        FormLayout.ResponsiveStep.LabelsPosition.TOP : FormLayout.ResponsiveStep.LabelsPosition.ASIDE;
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("1px", numColumns, labelsPosition));
        formLayout.setSizeUndefined();
        return formLayout;
    }
}
