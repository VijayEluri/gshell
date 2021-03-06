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

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import com.planet57.gshell.alias.AliasRegistry;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.util.io.IO;
import com.planet57.gshell.command.CommandActionSupport;
import com.planet57.gshell.util.cli2.Argument;
import com.planet57.gshell.util.cli2.CliProcessor;
import com.planet57.gshell.util.cli2.CliProcessorAware;
import com.planet57.gshell.util.jline.Complete;
import com.planet57.gshell.util.i18n.I18N;
import com.planet57.gshell.util.i18n.MessageBundle;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Define an alias or list defined aliases.
 *
 * @since 2.5
 */
@Command(name = "alias", description = "Define an alias or list defined aliases")
public class AliasAction
    extends CommandActionSupport
    implements CliProcessorAware
{
  private interface Messages
    extends MessageBundle
  {
    @DefaultMessage("No aliases have been defined")
    String noAliases();

    @DefaultMessage("Defined aliases:")
    String definedAliases();

    @DefaultMessage("Alias to: @{bold %s}")
    String aliasTarget(String target);

    @DefaultMessage("Missing argument: %s")
    String missinArgument(String name);
  }

  private static final Messages messages = I18N.create(Messages.class);

  private final AliasRegistry aliasRegistry;

  @Nullable
  @Argument(index = 0, description = "Name of the alias to define", token = "NAME")
  @Complete("alias-name")
  private String name;

  @Nullable
  @Argument(index = 1, description = "Target command to be aliased as NAME", token = "TARGET")
  private List<String> target;

  @Inject
  public AliasAction(final AliasRegistry aliasRegistry) {
    this.aliasRegistry = checkNotNull(aliasRegistry);
  }

  public void setProcessor(final CliProcessor processor) {
    checkNotNull(processor);
    processor.setStopAtNonOption(true);
  }

  @Override
  public Object execute(@Nonnull final CommandContext context) throws Exception {
    if (name == null) {
      listAliases(context.getIo());
    }
    else {
      checkArgument(target != null, messages.missinArgument("TARGET"));
      String alias = String.join(" ", target);
      log.debug("Defining alias: {} -> {}", name, alias);
      aliasRegistry.registerAlias(name, alias);
    }

    return null;
  }

  private void listAliases(final IO io) throws Exception {
    log.debug("Listing defined aliases");

    Map<String, String> aliases = aliasRegistry.getAliases();
    if (aliases.isEmpty()) {
      io.println(messages.noAliases());
    }
    else {
      io.println(messages.definedAliases());

      // find the maximum length of all alias names
      int maxNameLen = aliases.keySet().stream().mapToInt(String::length).max().orElse(0);
      String nameFormat = "%-" + maxNameLen + 's';

      aliases.forEach((key, value) -> {
        String formattedName = String.format(nameFormat, key);
        io.format("  @{bold %s} %s%n", formattedName, messages.aliasTarget(value));
      });
    }
  }
}
