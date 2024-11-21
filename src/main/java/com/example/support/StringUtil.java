
/*
 * Copyright (C) 2012 Archie L. Cobbs. All rights reserved.
 */

package com.example.support;

import com.google.common.base.Preconditions;

import java.util.function.Function;

import org.apache.commons.text.StringEscapeUtils;

public final class StringUtil {

    public static final String BLACK_RIGHTWARDS_ARROW = "\u27a1";
    public static final String BLOCK = "\u2588";
    public static final String BULLET = "\u2022";
    public static final String CHECK_GREEN_BACKGROUND = "\u2705";
    public static final String CHECK_MARK = "\u2713";
    public static final String CLOSED_LOCK = "\ud83d\udd12";
    public static final String CROSS_MARK = "\u2715";
    public static final String EYE = "\ud83d\udc41";
    public static final String FULL_BLOCK = "\u2588";
    public static final String GREATER_THAN_OR_EQUAL_TO = "\u2265";
    public static final String HEAVY_EXCLAMATION_MARK_SYMBOL = "\u2757";
    public static final String HEAVY_TRIANGLE_HEADED_RIGHTWARDS_ARROW = "\u279e";
    public static final String HEAVY_WIDE_HEADED_RIGHTWARDS_ARROW = "\u2794";
    public static final String INFORMATION_SOURCE = "\u24d8";
    public static final String IN_ARROW = "\u21d8";
    public static final String LESS_THAN_OR_EQUAL_TO = "\u2264";
    public static final String NON_BREAKING_SPACE = "\u00a0";
    public static final String OPEN_LOCK = "\ud83d\udd13";
    public static final String OUT_ARROW = "\u21d7";
    public static final String PENCIL = "\u270f\ufe0f";
    public static final String THUMBS_UP = "\ud83d\udc4d";
    public static final String UPPER_RIGHT_ARROW = "\u2197";
    public static final String VIDEOCASSETTE = "\ud83d\udcfc";
    public static final String WARNING_TRIANGLE = "\u26A0\ufe0f";
    public static final String WHITE_RIGHT_POINTING_INDEX = "\u261e";
    public static final String WHITE_RIGHT_POINTING_SMALL_TRIANGLE = "\u25B9";
    public static final String LEFT_POINTING_DOUBLE_ANGLE_QUOTE = "\u00ab";              // "«"
    public static final String RIGHT_POINTING_DOUBLE_ANGLE_QUOTE = "\u00bb";             // "»"
    public static final String PAPER_CLIP = "\ud83d\udcce";
    public static final String FAST_FORWARD_SYMBOL = "\u23e9";
    public static final String REWIND_SYMBOL = "\u23ea";
    public static final String SKIP_FORWARD_SYMBOL = "\u23ed\ufe0f";
    public static final String SKIP_BACKWARD_SYMBOL = "\u23ee\ufe0f";
    public static final String ORANGE_CIRCLE = "\ud83d\udfe0";
    public static final String YELLOW_CIRCLE = "\ud83d\udfe1";
    public static final String RED_CIRCLE = "\ud83d\udd34";
    public static final String GREEN_CIRCLE = "\ud83d\udfe2";
    public static final String WHITE_CIRCLE = "\u26aa\ufe0f";
    public static final String NO_ENTRY = "\u26d4\ufe0f";

    private StringUtil() {
    }

    /**
     * Make string HTML safe.
     */
    public static String escapeHtml(String s) {
        return StringEscapeUtils.escapeHtml4(s);
    }

    /**
     * Make string HTML safe and wrap in a {@code &lt;span&gt;} element.
     */
    public static String toHtmlSpan(String s) {
        return "<span>" + StringEscapeUtils.escapeHtml4(s) + "</span>";
    }

    /**
     * Augment the given map by intercepting null or empty strings and returning HTML <i>Empty</i>.
     *
     * @param function function that maps a string into HTML
     * @return function that is the same as {@code function} except null/empty strings are mapped to <i>Empty</i>
     * @throws IllegalArgumentException if {@code function} is null
     */
    public static Function<String, String> orHtmlEmpty(Function<String, String> function) {
        Preconditions.checkArgument(function != null, "null function");
        return s -> s == null || s.isEmpty() ? "<i>Empty</i>" : function.apply(s);
    }

    /**
     * Convert the given string to HTML using a fixed-width font.
     *
     * @param text the non-HTML text to display
     * @return fixed-width font HTML
     * @throws IllegalArgumentException if {@code text} is null
     */
    public static String toHtmlCode(String text) {
        Preconditions.checkArgument(text != null, "null text");
        return "<code>" + StringUtil.escapeHtml(text) + "</code>";
    }

    /**
     * Convert the {@link Enum#name name()} of the given {@link Enum} value to HTML using a fixed-width font.
     *
     * @param value enum value
     * @return fixed-width font HTML
     * @throws IllegalArgumentException if {@code value} is null
     */
    public static String toHtmlCode(Enum<?> value) {
        Preconditions.checkArgument(value != null, "null value");
        return StringUtil.toHtmlCode(value.name());
    }

    /**
     * Convert the given string to HTML using a boldface font.
     *
     * @param text the non-HTML text to display
     * @return boldface HTML
     * @throws IllegalArgumentException if {@code text} is null
     */
    public static String toHtmlBold(String text) {
        Preconditions.checkArgument(text != null, "null text");
        return "<b>" + StringUtil.escapeHtml(text) + "</b>";
    }

    /**
     * Convert the given string to HTML using an italics font.
     *
     * @param text the non-HTML text to display
     * @return italics HTML
     * @throws IllegalArgumentException if {@code text} is null
     */
    public static String toHtmlItalic(String text) {
        Preconditions.checkArgument(text != null, "null text");
        return "<i>" + StringUtil.escapeHtml(text) + "</i>";
    }

    /**
     * Convert the given string to HTML with the specified color.
     *
     * @param color HTML color
     * @param text the non-HTML text to display
     * @return italics HTML
     * @throws IllegalArgumentException if {@code color} or {@code text} is null
     */
    public static String toHtmlColor(String color, String text) {
        Preconditions.checkArgument(color != null, "null color");
        Preconditions.checkArgument(text != null, "null text");
        return "<font color=\"" + color + "\">" + StringUtil.escapeHtml(text) + "</font>";
    }
}
