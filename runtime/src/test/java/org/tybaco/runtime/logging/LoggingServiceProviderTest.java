package org.tybaco.runtime.logging;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoggingServiceProviderTest {

  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final LoggingServiceProvider serviceProvider = new LoggingServiceProvider(outputStream);

  @Test
  void logSimple() throws Exception {
    var loggerFactory = serviceProvider.getLoggerFactory();
    var logger = loggerFactory.getLogger("abc");
    logger.info("Hello");
    Thread.sleep(100L);
    outputStream.writeTo(System.out);
  }

  @BeforeAll
  void beforeAll() {
    serviceProvider.initialize();
  }

  @AfterAll
  void afterAll() {
    serviceProvider.close();
  }
}
