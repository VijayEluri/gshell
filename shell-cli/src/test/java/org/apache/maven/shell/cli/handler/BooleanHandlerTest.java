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

package org.apache.maven.shell.cli.handler;

import org.apache.maven.shell.cli.CommandLineProcessor;
import org.apache.maven.shell.cli.Option;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link BooleanHandler} class.
 *
 * @version $Rev$ $Date$
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class BooleanHandlerTest
{
    TestBean bean;
    
    CommandLineProcessor clp;

    @Before
    public void setUp() {
        bean = new TestBean();
        clp = new CommandLineProcessor(bean);
        
        assertEquals(2, clp.getOptionHandlers().size());
        assertEquals(0, clp.getArgumentHandlers().size());
    }

    @After
    public void tearDown() {
        bean = null;
        clp = null;
    }

    @Test
    public void test1() throws Exception {
        clp.process("-1");

        assertTrue(bean.flag);
    }

    @Test
    public void test2() throws Exception {
        clp.process("-2", "false");

        assertFalse(bean.flag2);

        clp.process("-2", "true");

        assertTrue(bean.flag2);
    }

    private static class TestBean
    {
        @Option(name="-1")
        boolean flag;

        @Option(name="-2", argumentRequired=true)
        boolean flag2;
    }
}