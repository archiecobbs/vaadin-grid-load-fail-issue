
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.support;

import org.apache.commons.text.StringEscapeUtils;

public final class StringUtil {

    private StringUtil() {
    }

    /**
     * Make string HTML safe.
     */
    public static String escapeHtml(String s) {
        return StringEscapeUtils.escapeHtml4(s);
    }
}
