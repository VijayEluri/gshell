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
package com.planet57.gshell.testharness;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.planet57.gshell.util.io.IO;
import com.planet57.gshell.util.io.StreamSet;
import org.jline.terminal.Terminal;
import org.slf4j.Logger;

/**
 * Buffering {@link IO} to capture output for assertions.
 *
 * @since 3.0
 */
public class BufferIO
    extends IO
{
  private ByteArrayOutputStream output;

  private ByteArrayOutputStream error;

  public BufferIO(final Terminal terminal) {
    this(new ByteArrayOutputStream(), new ByteArrayOutputStream(), terminal);
  }

  private BufferIO(final ByteArrayOutputStream output, final ByteArrayOutputStream error, final Terminal terminal) {
    super(new StreamSet(System.in, new PrintStream(output), new PrintStream(error)), terminal);
    this.output = output;
    this.error = error;
  }

  public ByteArrayOutputStream getOutput() {
    return output;
  }

  public String getOutputString() {
    return new String(getOutput().toByteArray());
  }

  public ByteArrayOutputStream getError() {
    return error;
  }

  public String getErrorString() {
    return new String(getError().toByteArray());
  }

  /**
   * Dump output streams to logging.
   */
  public void dump(final Logger logger) {
    String out = getOutputString();
    if (!out.trim().isEmpty()) {
      logger.debug("OUT:\n-----8<-----\n{}\n----->8-----", out);
    }

    String err = getErrorString();
    if (!err.trim().isEmpty()) {
      logger.debug("ERR:\n-----8<-----\n{}\n----->8-----", err);
    }
  }
}
