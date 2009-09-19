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

package org.apache.maven.shell.commands.basic;

import org.apache.maven.shell.History;
import org.apache.maven.shell.Shell;
import org.apache.maven.shell.cli.Argument;
import org.apache.maven.shell.command.Command;
import org.apache.maven.shell.command.CommandContext;
import org.apache.maven.shell.command.CommandSupport;
import org.apache.maven.shell.io.IO;
import org.codehaus.plexus.component.annotations.Component;

import java.util.List;

/**
 * Recall history.
 *
 * @version $Rev$ $Date$
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=Command.class, hint="recall")
public class RecallHistoryCommand
    extends CommandSupport
{
    @Argument(required=true)
    private int index;

    public Object execute(final CommandContext context) throws Exception {
        assert context != null;
        IO io = context.getIo();
        History history = context.getShell().getHistory();

        List<String> elements = history.elements();
        if (index < 0 || index >= elements.size()) {
            io.error(getMessages().format("error.no-such-index", index));
            return Result.FAILURE;
        }

        String element = elements.get(index);
        log.debug("Recalling from history: {}", element);
        
        Shell shell = context.getShell();
        return shell.execute(element);
    }
}