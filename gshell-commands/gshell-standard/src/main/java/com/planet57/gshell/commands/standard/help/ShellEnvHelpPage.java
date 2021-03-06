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
package com.planet57.gshell.commands.standard.help;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.planet57.gshell.help.HelpContentLoader;
import com.planet57.gshell.help.MetaHelpPage;

/**
 * Meta-help page for {@code shell-env}.
 *
 * @since 3.0
 */
@Named
@Singleton
public class ShellEnvHelpPage
  extends MetaHelpPage
{
  @Inject
  public ShellEnvHelpPage(final HelpContentLoader loader) {
    super("shell-env", "com.planet57.gshell.commands.standard.help.shell-env", loader);
  }

  @Override
  public String getDescription() {
    return "Shell environment overview";
  }
}
