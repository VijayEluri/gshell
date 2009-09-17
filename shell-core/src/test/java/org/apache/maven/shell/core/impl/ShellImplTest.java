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

package org.apache.maven.shell.core.impl;

import org.apache.maven.shell.Shell;
import org.apache.maven.shell.VariableNames;
import org.apache.maven.shell.registry.CommandRegistry;
import org.apache.maven.shell.testsupport.PlexusTestSupport;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link ShellImpl}.
 *
 * @version $Rev$ $Date$
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ShellImplTest
    implements VariableNames
{
    private PlexusTestSupport plexus;

    @Before
    public void setUp() throws Exception {
        plexus = new PlexusTestSupport(this);

        System.setProperty(MVNSH_HOME, System.getProperty("user.dir"));
        System.setProperty(MVNSH_USER_HOME, System.getProperty("user.dir"));
    }

    @After
    public void tearDown() {
        plexus.destroy();
        plexus = null;
    }

    @Test
    public void testBoot() throws Exception {
        Shell shell = plexus.lookup(Shell.class);
        assertNotNull(shell);
    }

    @Test
    public void testExecuteUnknownHi() throws Exception {
        Shell shell = plexus.lookup(Shell.class);
        assertNotNull(shell);

        try {
            shell.execute("hi");
            fail();
        }
        catch (Exception ignore) {
            // expected
        }
    }

    @Test
    public void testLookupSingleton() throws Exception {
        CommandRegistry r1 = plexus.lookup(CommandRegistry.class);
        assertNotNull(r1);

        CommandRegistry r2 = plexus.lookup(CommandRegistry.class);
        assertNotNull(r2);

        assertEquals(r1, r2);
    }
}