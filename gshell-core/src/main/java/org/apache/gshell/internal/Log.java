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

package org.apache.gshell.internal;

/**
 * Intenal logger.  For use by classes which can not use Slf4j due to bootstrap+logging configuration issues.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 2.0
 */
public final class Log
{
    ///CLOVER:OFF

    public static enum Level
    {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    @SuppressWarnings({ "StringConcatenation" })
    public static final boolean DEBUG = Boolean.getBoolean(Log.class.getName() + ".debug");

    @SuppressWarnings({ "StringConcatenation" })
    public static final boolean TRACE = Boolean.getBoolean(Log.class.getName() + ".trace");

    private static void print(final Object message) {
        if (message instanceof Throwable) {
            ((Throwable)message).printStackTrace();
        }
        else if (message.getClass().isArray()) {
            Object[] array = (Object[])message;

            for (int i=0; i<array.length; i++) {
                System.err.print(array[i]);
                if (i+1<array.length) {
                    System.err.print(",");
                }
            }
        }
        else {
            System.err.print(message);
        }
    }

    private static void log(final Level level, final Object[] messages) {
        synchronized (System.err) {
            System.err.format("[%s] ", level);

            for (Object message : messages) {
                print(message);
            }

            System.err.println();
            System.err.flush();
        }
    }

    public static void trace(final Object... messages) {
        if (TRACE) {
            log(Level.TRACE, messages);
        }
    }

    public static void debug(final Object... messages) {
        if (TRACE || DEBUG) {
            log(Level.DEBUG, messages);
        }
    }

    public static void warn(final Object... messages) {
        synchronized (System.err) {
            log(Level.WARN, messages);
        }
    }

    public static void error(final Object... messages) {
        synchronized (System.err) {
            log(Level.ERROR, messages);
        }
    }
}