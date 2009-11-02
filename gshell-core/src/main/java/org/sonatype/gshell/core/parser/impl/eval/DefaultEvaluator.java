/*
 * Copyright (C) 2009 the original author(s).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.gshell.core.parser.impl.eval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.gshell.ShellHolder;
import org.sonatype.gshell.Variables;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Evaluates expressions using regular expressions.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class DefaultEvaluator
    implements Evaluator
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    public Object eval(final String expression) throws Exception {
        // expression could be null

        // Skip interpolation if null or there is no start token
        if (expression == null || !expression.contains("${")) {
            return expression;
        }

        String input = expression;
        Matcher matcher = PATTERN.matcher(input);

        while (matcher.find()) {
            String key = matcher.group(1);

            Object rep = null;

            if (rep == null) {
                Variables vars = ShellHolder.get().getVariables();
                rep = vars.get(key);
            }

            if (rep == null) {
                rep = System.getProperty(key);
            }

            if (rep != null) {
                input = input.replace(matcher.group(0), rep.toString());
                matcher.reset(input);
            }
        }

        return input;
    }
}