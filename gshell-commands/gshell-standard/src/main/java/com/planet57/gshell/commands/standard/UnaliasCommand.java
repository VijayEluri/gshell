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
package com.planet57.gshell.commands.standard;

import javax.inject.Inject;
import javax.inject.Named;

import com.planet57.gshell.alias.AliasRegistry;
import com.planet57.gshell.alias.NoSuchAliasException;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.command.IO;
import com.planet57.gshell.command.support.CommandActionSupport;
import com.planet57.gshell.util.cli2.Argument;
import jline.console.completer.Completer;

/**
 * Undefine an alias.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.5
 */
@Command(name = "unalias")
public class UnaliasCommand
    extends CommandActionSupport
{
  private final AliasRegistry aliasRegistry;

  @Argument(index = 0, required = true)
  private String name;

  @Inject
  public UnaliasCommand(final AliasRegistry aliasRegistry) {
    assert aliasRegistry != null;
    this.aliasRegistry = aliasRegistry;
  }

  @Inject
  public UnaliasCommand installCompleters(@Named("alias-name") final Completer c1) {
    assert c1 != null;
    setCompleters(c1, null);
    return this;
  }

  public Object execute(final CommandContext context) {
    assert context != null;
    IO io = context.getIo();

    log.debug("Un-defining alias: {}", name);

    try {
      aliasRegistry.removeAlias(name);

      return Result.SUCCESS;
    }
    catch (NoSuchAliasException e) {
      io.error(getMessages().format("error.alias-not-defined", name));
      return Result.FAILURE;
    }
  }
}