package org.tybloco.runtime.logging;

/*-
 * #%L
 * runtime
 * %%
 * Copyright (C) 2023 Montoni
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import org.slf4j.event.Level;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;

import static java.nio.charset.StandardCharsets.UTF_8;

final class LoggingStream extends PrintStream {

  LoggingStream(ArrayBlockingQueue<LogRecord> queue, FastMDCAdapter mdcAdapter) {
    super(new ByteArrayOutputStream() {
      @Override
      public synchronized void flush() {
        try {
          var message = new String(buf, 0, count, UTF_8).trim();
          buf = new byte[0];
          count = 0;
          var thread = Thread.currentThread();
          var time = Instant.ofEpochMilli(System.currentTimeMillis());
          queue.put(new LogRecord(Level.ERROR, thread, time, "stderr", null, message, new Object[0], null, mdcAdapter));
        } catch (Throwable e) {
          e.printStackTrace(System.err);
        }
      }
    }, true, UTF_8);
  }
}
