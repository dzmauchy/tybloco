package org.tybaco.ide;

/*-
 * #%L
 * ide
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

import org.tybaco.ide.splash.Splash;
import org.tybaco.ide.splash.SplashStatus;
import org.tybaco.logging.FastConsoleHandler;
import org.tybaco.logging.LoggingManager;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarInputStream;
import java.util.logging.LogManager;
import java.util.stream.Stream;

import static java.lang.System.setProperty;
import static java.util.concurrent.ForkJoinPool.commonPool;
import static java.util.logging.LogManager.getLogManager;
import static org.tybaco.ide.splash.Splash.renderSplash;
import static org.tybaco.ide.splash.Splash.updateSplash;

public class Ide {

  public static void main(String... args) throws Exception {
    renderSplash();
    initLogging();
    var logger = LogManager.getLogManager().getLogger("");
    logger.info("Initializing classpath");
    var libUrlsF = commonPool().submit(Ide::libUrls);
    var javafxUrlsF = commonPool().submit(Ide::javafxLibs);
    var libUrls = libUrlsF.join();
    var javafxUrls = javafxUrlsF.join();
    var allUrls = Stream.concat(libUrls.stream(), javafxUrls.stream()).toList();
    logger.info("Classpath initialized");
    updateSplash();
    var classLoader = new IdeClassLoader("ide", allUrls, Thread.currentThread().getContextClassLoader());
    Thread.currentThread().setContextClassLoader(classLoader);
    logger.info("Preparing UI");
    bootstrapSplash(classLoader);
    logger.info("UI prepared");
    invokeMain(classLoader, args);
    logger.info("UI launched");
    Runtime.getRuntime().addShutdownHook(new Thread(classLoader::close));
  }

  private static void bootstrapSplash(URLClassLoader classLoader) throws Exception {
    updateSplash();
    var mainClass = classLoader.loadClass("org.tybaco.ui.Main");
    var updateSplash = mainClass.getField("updateSplash");
    updateSplash.set(null, (Runnable) Splash::updateSplash);
    var updateSplashStatus = mainClass.getField("updateSplashStatus");
    updateSplashStatus.set(null, (Runnable) SplashStatus::updateSplashStatus);
    var splashBeanPostProcessor = classLoader.loadClass("org.tybaco.ui.splash.SplashBeanPostProcessor");
    var incrementStep = splashBeanPostProcessor.getField("incrementStep");
    incrementStep.set(null, (Runnable) SplashStatus::incrementStep);
    var processorUpdateSplash = splashBeanPostProcessor.getField("updateSplash");
    processorUpdateSplash.set(null, (Runnable) () -> Splash.updateSplash(false));
  }

  private static void invokeMain(URLClassLoader classLoader, String... args) throws Exception {
    var mainClass = classLoader.loadClass("org.tybaco.ui.Main");
    var mainMethod = mainClass.getMethod("main", String[].class);
    mainMethod.invoke(null, (Object) args);
  }

  private static List<URL> javafxLibs() throws Exception {
    var classifier = classifier();
    var artifacts = new String[]{
      "base",
      "graphics",
      "controls",
      "media",
      "web"
    };
    var version = "20.0.2";
    var list = new LinkedList<URL>();
    for (var a : artifacts) {
      var base = "https://repo.maven.apache.org/maven2/org/openjfx/javafx-" + a;
      var url = base + "/" + version + "/" + "javafx-" + a + "-" + version + "-" + classifier + ".jar";
      list.add(new URI(url).toURL());
    }
    updateSplash();
    return list;
  }

  private static List<URL> libUrls() throws Exception {
    var url = Ide.class.getProtectionDomain().getCodeSource().getLocation();
    var excludedLibs = new HashSet<String>();
    try (var jarInputStream = new JarInputStream(url.openStream())) {
      var manifest = jarInputStream.getManifest();
      var mainAttributes = manifest.getMainAttributes();
      var classPath = mainAttributes.getValue("Class-Path");
      for (var entry : classPath.split(" ")) {
        if (entry.startsWith("lib/") && entry.endsWith(".jar")) {
          excludedLibs.add(entry.substring("lib/".length()));
        }
      }
    }
    var libPath = Path.of(url.toURI()).getParent().resolve("lib");
    var list = new LinkedList<URL>();
    try (var ds = Files.newDirectoryStream(libPath, "*.jar")) {
      for (var lib : ds) {
        if (!excludedLibs.contains(lib.getFileName().toString())) {
          list.add(lib.toUri().toURL());
        }
      }
    }
    return list;
  }

  private static String classifier() {
    var osArch = System.getProperty("os.arch");
    var osName = System.getProperty("os.name");
    if (osName.equalsIgnoreCase("linux")) {
      return osArch.equals("aarch64") ? "linux-aarch64" : "linux";
    } else if (osName.equalsIgnoreCase("windows")) {
      return "win";
    } else {
      return osArch.equals("aarch64") ? "mac-aarch64" : "mac";
    }
  }

  private static void initLogging() {
    setProperty("java.util.logging.manager", LoggingManager.class.getName());
    var rootLogger = getLogManager().getLogger("");
    rootLogger.addHandler(new FastConsoleHandler());
  }

  private static final class IdeClassLoader extends URLClassLoader {

    static {
      registerAsParallelCapable();
    }

    private IdeClassLoader(String name, List<URL> urls, ClassLoader parent) {
      super(name, urls.toArray(URL[]::new), parent);
    }

    @Override
    public void close() {
      try {
        super.close();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }
  }
}