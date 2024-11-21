
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.gui;

import com.example.gui.support.UIUtil;
import com.example.support.StringUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Information about one config version.
 */
public final class ConfigInfo {

    public static final String NAME_PROPERTY = "name";
    public static final String UUID_PROPERTY = "uuid";
    public static final String HTML_NAME_PROPERTY = "htmlName";
    public static final String STATUS_PROPERTY = "status";
    public static final String RUNNING_PROPERTY = "running";
    public static final String SIGNED_OFF_PROPERTY = "signedOff";
    public static final String TIMESTAMP_PROPERTY = "timestamp";
    public static final String USERNAME_PROPERTY = "username";
    public static final String COMMENT_PROPERTY = "comment";
    public static final String SERVER_NAME_PROPERTY = "serverName";

    private final String revision;
    private final String serverName;
    private final Status status;
    private final boolean signedOff;
    private final boolean running;

// Constructors

    public ConfigInfo(String revision, String serverName, Status status, boolean signedOff, boolean running) {
        this.revision = revision;
        this.serverName = serverName;
        this.status = status;
        this.signedOff = signedOff;
        this.running = running;
    }

// Accessors

    public String getRevision() {
        return this.revision;
    }

    public boolean isActive() {
        return this.status.equals(Status.ACTIVE);
    }

    public Status getStatus() {
        return this.status;
    }

    public boolean isRunning() {
        return this.running;
    }

    public String getServerName() {
        return this.serverName;
    }

    public boolean isSignedOff() {
        return this.signedOff;
    }

// DataProvider Properties

    public HtmlSpan propertyUUID() {
        if (this.isActive())
            return null;
        return new HtmlSpan(StringUtil.toHtmlCode(UUID.randomUUID().toString()));
    }

    public String propertyName() {
        return this.revision;
    }

    public Component propertyHtmlName() {
        return this.isActive() ?
          new HtmlSpan(StringUtil.toHtmlItalic(this.revision)) :
          new Text(this.revision);
    }

    public Component propertyRunning() {
        if (this.isActive() && !this.running)
            return UIUtil.getLabel(StringUtil.WARNING_TRIANGLE, "The current active configuration is unknown");
        if (!this.isActive() && this.running)
            return UIUtil.getLabel(StringUtil.CHECK_GREEN_BACKGROUND, "This configuration is currently active");
        return null;
    }

    public Component propertyStatus() {
        return this.status.toComponent();
    }

    public String propertySignedOff() {
        return this.signedOff ? StringUtil.THUMBS_UP : null;
    }

    public String propertyTimestamp() {
        return new SimpleDateFormat("MMM d, yyyy HH:mm:ss").format(new Date());
    }

    public HtmlSpan propertyUsername() {
        return new HtmlSpan(StringUtil.toHtmlCode("fred"));
    }

    public Html propertyComment() {
        return Optional.ofNullable("This is some comment.")
          .map(StringUtil::toHtmlItalic)
          .map(Html::new)
          .orElse(null);
    }

    public Html propertyServerName() {
        return Optional.ofNullable(this.serverName)
          .map(StringUtil::toHtmlBold)
          .map(Html::new)
          .orElse(null);
    }

// Status

    public enum Status {
        PLAIN(null, null),
        SEALED(StringUtil.CLOSED_LOCK, "Configuration is sealed and can no longer be edited"),
        EDITING(StringUtil.PENCIL, "This is the configuration currently being edited"),
        VIEWING(StringUtil.EYE, "This is the configuration currently being viewed"),
        ACTIVE(StringUtil.CHECK_GREEN_BACKGROUND, "This is the currently active configuration");

        private final String emoticon;
        private final String toolTip;

        Status(String emoticon, String toolTip) {
            this.emoticon = emoticon;
            this.toolTip = toolTip;
        }

        public Component toComponent() {
            return this.emoticon != null ? UIUtil.getLabel(this.emoticon, this.toolTip) : null;
        }
    }
}
