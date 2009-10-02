/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.gshell.ansi;

import static org.apache.gshell.ansi.Ansi.Color;
import static org.apache.gshell.ansi.Ansi.Attribute;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Renders ANSI color escape-codes in strings by parsing out some special syntax to pick up the correct fluff to use.
 *
 * <p>
 * The syntax for embedded ANSI codes is:
 * 
 * <pre>
 *  @|<code>(,<code>)*<space><text>|
 * </pre>
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 2.0
 */
public class AnsiRenderer
{
    public static final String BEGIN_TOKEN = "@|";

    public static final String END_TOKEN = "|";

    public static final String CODE_TEXT_SEPARATOR  = " ";

    public static final String CODE_LIST_SEPARATOR  = ",";

    private static final Pattern PATTERN = Pattern.compile("\\@\\|([^ ]+) ([^|]+)\\|");

    public String render(String input) {
        if (input != null) {
            Matcher matcher = PATTERN.matcher(input);

            while (matcher.find()) {
                String rep = render(matcher.group(2), matcher.group(1).split(CODE_LIST_SEPARATOR));
                if (rep != null) {
                    input = input.replace(matcher.group(0), rep);
                    matcher.reset(input);
                }
            }
        }

        return input;
    }

    private static enum Code
    {
        // Colors
        BLACK(Color.BLACK),
        RED(Color.RED),
        GREEN(Color.GREEN),
        YELLOW(Color.YELLOW),
        BLUE(Color.BLUE),
        MAGENTA(Color.MAGENTA),
        CYAN(Color.CYAN),
        WHITE(Color.WHITE),

        // Forground Colors
        FG_BLACK(Color.BLACK, false),
        FG_RED(Color.RED, false),
        FG_GREEN(Color.GREEN, false),
        FG_YELLOW(Color.YELLOW, false),
        FG_BLUE(Color.BLUE, false),
        FG_MAGENTA(Color.MAGENTA, false),
        FG_CYAN(Color.CYAN, false),
        FG_WHITE(Color.WHITE, false),

        // Background Colors
        BG_BLACK(Color.BLACK, true),
        BG_RED(Color.RED, true),
        BG_GREEN(Color.GREEN, true),
        BG_YELLOW(Color.YELLOW, true),
        BG_BLUE(Color.BLUE, true),
        BG_MAGENTA(Color.MAGENTA, true),
        BG_CYAN(Color.CYAN, true),
        BG_WHITE(Color.WHITE, true),

        // Attributes
        RESET(Attribute.RESET),
        INTENSITY_BOLD(Attribute.INTENSITY_BOLD),
        INTENSITY_FAINT(Attribute.INTENSITY_FAINT),
        ITALIC(Attribute.ITALIC),
        UNDERLINE(Attribute.UNDERLINE),
        BLINK_SLOW(Attribute.BLINK_SLOW),
        BLINK_FAST(Attribute.BLINK_FAST),
        BLINK_OFF(Attribute.BLINK_OFF),
        NEGATIVE_ON(Attribute.NEGATIVE_ON),
        NEGATIVE_OFF(Attribute.NEGATIVE_OFF),
        CONCEAL_ON(Attribute.CONCEAL_ON),
        CONCEAL_OFF(Attribute.CONCEAL_OFF),
        UNDERLINE_DOUBLE(Attribute.UNDERLINE_DOUBLE),
        INTENSITY_NORMAL(Attribute.INTENSITY_NORMAL),
        UNDERLINE_OFF(Attribute.UNDERLINE_OFF),

        // Aliases
        BOLD(Attribute.INTENSITY_BOLD),
        FAINT(Attribute.INTENSITY_FAINT),
        ;

        private final Enum n;

        private final boolean background;

        private Code(final Enum n, boolean background) {
            this.n = n;
            this.background = background;
        }

        private Code(final Enum n) {
            this(n, false);
        }

        public boolean isColor() {
            return n instanceof Color;
        }

        public Color getColor() {
            return (Color) n;
        }

        public boolean isAttribute() {
            return n instanceof Attribute;
        }

        public Attribute getAttribute() {
            return (Attribute) n;
        }

        public boolean isBackground() {
            return background;
        }
    }

    private String render(final String text, final String... codes) {
        org.fusesource.jansi.Ansi ansi = Ansi.ansi();

        for (String name : codes) {
            Code code = Code.valueOf(name.toUpperCase());

            if (code.isColor()) {
                if (code.isBackground()) {
                    ansi = ansi.bg(code.getColor());
                }
                else {
                    ansi = ansi.fg(code.getColor());
                }
            }
            else if (code.isAttribute()) {
                ansi = ansi.a(code.getAttribute());
            }
        }

        return ansi.a(text).reset().toString();
    }

    public static boolean test(final String text) {
        return text != null && text.contains(BEGIN_TOKEN);
    }
}
