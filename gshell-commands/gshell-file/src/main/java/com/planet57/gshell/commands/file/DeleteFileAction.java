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
package com.planet57.gshell.commands.file;

import java.io.File;

import javax.annotation.Nonnull;

import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.util.cli2.Option;
import com.planet57.gshell.util.io.FileAssert;
import com.planet57.gshell.util.cli2.Argument;
import com.planet57.gshell.util.io.FileSystemAccess;
import com.planet57.gshell.util.jline.Complete;

/**
 * Remove a file.
 *
 * @since 2.0
 */
@Command(name = "rm", description = "Remove a file")
public class DeleteFileAction
    extends FileCommandActionSupport
{
  @Argument(required = true, description = "The path of the file remove", token = "PATH")
  @Complete("file-name")
  private String path;

  /**
   * @since 3.0
   */
  @Option(name = "r", longName = "recursive", description = "Remove directories and their contents recursively")
  private boolean recursive;

  @Override
  public Object execute(@Nonnull final CommandContext context) throws Exception {
    FileSystemAccess fs = getFileSystem();
    File file = fs.resolveFile(path);

    new FileAssert(file).exists();

    if (recursive) {
      log.debug("Deleting directory: {}", file);
      new FileAssert((file)).isDirectory();
      fs.deleteDirectory(file);
    }
    else {
      log.debug("Deleting file: {}", file);
      new FileAssert(file).isFile();
      fs.deleteFile(file);
    }

    return null;
  }
}
