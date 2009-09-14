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

package org.apache.maven.shell.testsuite.basic;

import org.apache.maven.shell.command.Command;
import org.apache.maven.shell.testsuite.CommandTestSupport;

/**
 * Tests for the {@link EchoCommand}.
 *
 * @version $Rev$ $Date$
 */
public class EchoCommandTest
    extends CommandTestSupport
{
    public EchoCommandTest() {
        super("echo");
    }

    public void testDefault() throws Exception {
        Object result = execute(name);
        assertEquals(Command.Result.SUCCESS, result);
    }

    public void test_a_b_c() throws Exception {
        Object result = execute(name + " a b c");
        assertEquals(Command.Result.SUCCESS, result);
    }
}