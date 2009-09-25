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

package org.apache.gshell.testsuite.basic;

import org.apache.gshell.cli.ProcessingException;
import org.apache.gshell.testsuite.CommandTestSupport;
import org.apache.gshell.core.commands.UnsetCommand;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Tests for the {@link UnsetCommand}.
 * 
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class UnsetCommandTest
    extends CommandTestSupport
{
    public UnsetCommandTest() {
        super("unset", UnsetCommand.class);
    }

    @Override
    @Test
    public void testDefault() throws Exception {
        try {
            super.testDefault();
            fail();
        }
        catch (ProcessingException e) {
            // expected
        }
    }

    @Test
    public void testUndefineVariable() throws Exception {
        vars.set("foo", "bar");
        assertTrue(vars.contains("foo"));
        Object result = executeWithArgs("foo");
        assertEqualsSuccess(result);
        assertFalse(vars.contains("foo"));
    }

    @Test
    public void testUndefineUndefinedVariable() throws Exception {
        assertFalse(vars.contains("foo"));
        Object result = executeWithArgs("foo");

        // Unsetting undefined should not return any errors
        assertEqualsSuccess(result);
    }
}