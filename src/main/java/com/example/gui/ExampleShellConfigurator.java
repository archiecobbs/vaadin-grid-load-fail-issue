
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.example.web.Deployment;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Meta;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.Theme;

@Meta(name = "application-name", content = "Issue16775")
@Meta(name = "description", content = "Issue16775")
@Meta(name = "publisher", content = "Examples, LLC")
@PageTitle("Issue 16775 Reproducer")
@Push
@SuppressWarnings("serial")
@Theme(Deployment.PEXP_THEME_NAME)
public final class ExampleShellConfigurator implements AppShellConfigurator {
}
