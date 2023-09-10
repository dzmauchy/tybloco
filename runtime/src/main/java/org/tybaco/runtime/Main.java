package org.tybaco.runtime;

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

import org.tybaco.runtime.application.ApplicationLoader;
import org.tybaco.runtime.application.ApplicationRunner;
import org.tybaco.runtime.logging.LogConfigurer;
import org.tybaco.runtime.plugins.PluginLoader;

public final class Main {

  public static void main(String... args)  {
    execute("initLogging", new LogConfigurer());
    execute("loadPlugins", new PluginLoader());
    execute("loadApplication", new ApplicationLoader(args));
    execute("runApplication", new ApplicationRunner());
  }

  private static void execute(String step, Runnable task) {
    try {
      task.run();
    } catch (Throwable e) {
      throw new BootstrapException(step, e);
    }
  }
}
