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

package org.apache.maven.shell.commands.maven;

import org.apache.maven.cli.MavenCli;
import org.apache.maven.shell.VariableNames;
import org.apache.maven.shell.Variables;
import org.apache.maven.shell.Arguments;
import org.apache.maven.shell.command.Command;
import org.apache.maven.shell.command.CommandContext;
import org.apache.maven.shell.command.CommandSupport;
import org.apache.maven.shell.command.OpaqueArguments;
import org.apache.maven.shell.io.IO;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.StringUtils;

/**
 * Execute Maven.
 * 
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role= Command.class, hint="mvn")
public class MavenCommand
    extends CommandSupport
    implements OpaqueArguments, VariableNames
{
    // @Requirement
    // private MavenRuntime runtime;

    public Object execute(final CommandContext context) throws Exception {
        assert context != null;

        IO io = context.getIo();

        /*
        MavenExecutionRequest request = new DefaultMavenExecutionRequest();

        MavenExecutionResult result = runtime.getEmbedder().execute(request);

        if (result.hasExceptions()) {
            ExceptionSummary es = result.getExceptionSummary();

            if (es == null) {
                io.error("", result.getExceptions().getOutput(0));
            }
            else {
                //noinspection ThrowableResultOfMethodCallIgnored
                io.error(es.getMessage(), es.getException());
            }

            return Result.FAILURE;
        }
        */

        String[] args = Arguments.toStringArray(context.getArguments());

        // Propagate shell.user.dir to user.dir for MavenCLI
        Variables vars = context.getVariables();
        String dirname = vars.get(SHELL_USER_DIR, String.class);
        System.setProperty("user.dir", dirname);

        log.debug("Invoking maven with args: {}, in dir: {}", StringUtils.join(args, " "), dirname);

        ClassWorld classWorld = new ClassWorld("plexus.core", Thread.currentThread().getContextClassLoader());
        int result = MavenCli.main(args, classWorld);

        if (result == 0) {
            return Result.SUCCESS;    
        }
        else if (result == 1) {
            return Result.FAILURE;
        }
        else {
            return result;
        }
    }
}