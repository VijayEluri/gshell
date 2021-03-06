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
package com.planet57.gshell.internal;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.planet57.gshell.util.OperatingSystem;
import com.planet57.gshell.util.io.FileSystemAccess;
import com.planet57.gshell.variables.Variables;
import org.apache.commons.io.FileUtils;
import org.sonatype.goodies.common.ComponentSupport;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.planet57.gshell.variables.VariableNames.SHELL_HOME;
import static com.planet57.gshell.variables.VariableNames.SHELL_USER_DIR;
import static com.planet57.gshell.variables.VariableNames.SHELL_USER_HOME;

/**
 * Default {@link FileSystemAccess} component.
 *
 * @since 2.3
 */
@Named
@Singleton
public class FileSystemAccessImpl
    extends ComponentSupport
    implements FileSystemAccess
{
  private final Provider<Variables> variables;

  @Inject
  public FileSystemAccessImpl(final Provider<Variables> variables) {
    this.variables = checkNotNull(variables);
  }

  @Override
  public File resolveDir(final String name) throws IOException {
    checkNotNull(name);
    return variables.get().require(name, File.class).getCanonicalFile();
  }

  @Override
  public File getShellHomeDir() throws IOException {
    return resolveDir(SHELL_HOME);
  }

  @Override
  public File getUserDir() throws IOException {
    return resolveDir(SHELL_USER_DIR);
  }

  @Override
  public void setUserDir(final File dir) {
    log.debug("Changing user-dir: {}", dir);

    String path = dir.getPath();
    variables.get().set(SHELL_USER_DIR, path);

    // HACK: adjust to user.dir; for better general compatibility may want to put on the shell.* versions of thsese?
    System.setProperty("user.dir", path);
  }

  @Override
  public File getUserHomeDir() throws IOException {
    return resolveDir(SHELL_USER_HOME);
  }

  @Override
  public File resolveFile(@Nullable File baseDir, @Nullable final String path) throws IOException {
    File userDir = getUserDir();

    if (baseDir == null) {
      baseDir = userDir;
    }

    File file;
    if (path == null) {
      file = baseDir;
    }
    else if (path.startsWith("~")) {
      File userHome = getUserHomeDir();
      file = new File(userHome.getPath() + path.substring(1));
    }
    else {
      file = new File(path);
    }

    // support paths like "<drive>:" and "/" on windows
    if (OperatingSystem.WINDOWS) {
      if (path != null && path.equals("/")) {
        // Get the current canonical path to access drive root
        String tmp = new File(".").getCanonicalPath().substring(0, 2);
        return new File(tmp + "/").getCanonicalFile();
      }

      String tmp = file.getPath();
      if (tmp.length() == 2 && tmp.charAt(1) == ':') {
        // Have to append "/" on windows it seems to get the right root
        return new File(tmp + "/").getCanonicalFile();
      }
    }

    if (!file.isAbsolute()) {
      file = new File(baseDir, file.getPath());
    }

    return file.getCanonicalFile();
  }

  @Override
  public File resolveFile(final String path) throws IOException {
    return resolveFile(null, path);
  }

  @Override
  public boolean hasChildren(final File file) {
    checkNotNull(file);

    if (file.isDirectory()) {
      File[] children = file.listFiles();

      if (children != null && children.length != 0) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void mkdir(final File dir) throws IOException {
    checkNotNull(dir);
    log.debug("Create directory: {}", dir);
    FileUtils.forceMkdir(dir);
  }

  @Override
  public void deleteDirectory(final File dir) throws IOException {
    checkNotNull(dir);
    log.debug("Delete directory: {}", dir);
    FileUtils.deleteDirectory(dir);
  }

  @Override
  public void deleteFile(final File file) throws IOException {
    checkNotNull(file);
    log.debug("Delete file: {}", file);
    FileUtils.forceDelete(file);
  }

  @Override
  public void copyFile(final File source, final File target) throws IOException {
    checkNotNull(source);
    checkNotNull(target);
    log.debug("Copy file {} -> {}", source, target);
    FileUtils.copyFile(source, target);
  }

  @Override
  public void copyDirectory(final File source, final File target) throws IOException {
    checkNotNull(source);
    checkNotNull(target);
    log.debug("Copy directory: {} -> {}", source, target);
    FileUtils.copyDirectory(source, target);
  }

  @Override
  public void copyToDirectory(final File source, final File target) throws IOException {
    checkNotNull(source);
    checkNotNull(target);
    log.debug("Copy file to directory: {} -> {}", source, target);
    FileUtils.copyFileToDirectory(source, target);
  }
}
