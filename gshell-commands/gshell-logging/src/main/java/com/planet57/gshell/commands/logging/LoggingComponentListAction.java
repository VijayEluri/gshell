/*
 * Copyright (c) 2009-present the original author or authors.
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
package com.planet57.gshell.commands.logging;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.util.io.IO;
import com.planet57.gshell.logging.LoggingComponent;
import com.planet57.gshell.util.cli2.Option;

/**
 * List components.
 *
 * @since 2.5
 */
@Command(name = "logging/components", description = "List logging components")
public class LoggingComponentListAction
  extends LoggingCommandActionSupport
{
  @Nullable
  @Option(name = "n", longName = "name", description = "Include components matching NAME", token = "NAME")
  private String nameQuery;

  @Nullable
  @Option(name = "t", longName = "type", description = "Include components matching TYPE", token = "TYPE")
  private String typeQuery;

  @Option(name = "v", longName = "verbose", description = "Display verbose details")
  private boolean verbose;

  @Override
  public Object execute(@Nonnull final CommandContext context) throws Exception {
    IO io = context.getIo();

    for (LoggingComponent component : getLogging().getComponents()) {
      if ((typeQuery == null || component.getType().contains(typeQuery)) &&
          (nameQuery == null || component.getName().contains(nameQuery))) {
        io.println(component);
        if (verbose) {
          io.format("  %s%n", component.getTarget());
        }
      }
    }

    return null;
  }
}
