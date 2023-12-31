package org.tybloco.runtime.application.tasks;

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

import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.tybloco.runtime.application.ApplicationContext;
import org.tybloco.runtime.application.ApplicationTask;
import org.tybloco.runtime.logging.LoggingManager;
import org.tybloco.runtime.logging.LoggingServiceProvider;

public final class LogConfigurer implements ApplicationTask {

  @Override
  public void run(ApplicationContext context) {
    System.setProperty("java.util.logging.manager", LoggingManager.class.getName());
    System.setProperty("slf4j.provider", LoggingServiceProvider.class.getName());
    if (!SLF4JBridgeHandler.isInstalled()) SLF4JBridgeHandler.install();
    var loggerFactory = LoggerFactory.getILoggerFactory();
    if (loggerFactory == null) throw new IllegalStateException("Logger factory cannot be null");
  }
}
