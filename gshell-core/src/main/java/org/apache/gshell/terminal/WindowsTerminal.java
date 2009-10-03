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

package org.apache.gshell.terminal;

import java.io.File;

/**
 * Windows terminal.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 2.0
 */
public class WindowsTerminal
    extends jline.WindowsTerminal
{
    ///CLOVER:OFF


    @Override
    public void initializeTerminal() throws Exception {
        // HACK: Nuke jline dll, need to fix the impl to be unique
        String version = getClass().getPackage().getImplementationVersion();
        if (version == null) {
            version = "";
        }
        version = version.replace('.', '_');
        File file = new File(System.getProperty("java.io.tmpdir"), "jline_" + version + ".dll");
        file.delete();

        super.initializeTerminal();
    }

    @Override
    public boolean isANSISupported() {
        return true;
    }

    @Override
    public int getTerminalWidth() {
        int width = super.getTerminalWidth();
        if (width < 1) {
            width = 80;
        }
        return width;
    }
}