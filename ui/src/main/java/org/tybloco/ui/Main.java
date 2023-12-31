package org.tybloco.ui;

/*-
 * #%L
 * ui
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

import javafx.application.Application;
import org.tybloco.logging.FastConsoleHandler;
import org.tybloco.logging.LoggingManager;
import org.tybloco.ui.lib.logging.UILogHandler;
import org.tybloco.ui.main.MainApplication;

import static java.lang.System.setProperty;
import static java.util.Arrays.stream;
import static java.util.logging.LogManager.getLogManager;

public final class Main {

  public static volatile Runnable updateSplash = () -> {};
  public static volatile Runnable updateSplashStatus = () -> {};

  public static void main(String... args) {
    System.setProperty("com.sun.javafx.gestures.scroll", "true");
    updateSplash.run();
    initLogging();
    updateSplash.run();
    Application.launch(MainApplication.class, args);
  }

  public static void initLogging() {
    setProperty("java.util.logging.manager", LoggingManager.class.getName());
    var rootLogger = getLogManager().getLogger("");
    if (stream(rootLogger.getHandlers()).noneMatch(FastConsoleHandler.class::isInstance)) {
      rootLogger.addHandler(new FastConsoleHandler());
    }
    rootLogger.addHandler(new UILogHandler());
    rootLogger.info("Logging initialized");
  }
}
