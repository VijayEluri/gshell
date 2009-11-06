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

package org.sonatype.gshell.core;

import java.util.EventObject;

/**
 * Event fired once a variable has been set.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.0
 */
public class VariableSetEvent
    extends EventObject
{
    ///CLOVER:OFF

    private final String name;

    private final Object previous;

    public VariableSetEvent(final String name, final Object previous) {
        super(name);

        assert name != null;
        // previous could be null

        this.name = name;
        this.previous = previous;
    }

    public String getName() {
        return name;
    }

    public Object getPrevious() {
        return previous;
    }
}