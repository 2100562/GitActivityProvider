package pt.uab.meiw.aps;

import io.helidon.config.Config;
import io.helidon.http.media.jackson.JacksonSupport;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.accesslog.AccessLogFeature;
import io.helidon.webserver.cors.CorsSupport;
import io.helidon.webserver.http.HttpRouting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Git Activity Provider main class. It is responsible for loading the
 * configuration, bootstrap all required components and start the Helidon
 * Webserver.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ActivityProvider {

  private static final Logger LOG = LoggerFactory.getLogger(
      ActivityProvider.class);

  public static void main(String[] args) {
    LOG.info("Starting Git Activity Provider");

    LOG.info("Loading configuration");
    final var config = Config.global();

    final var factory = new AbstractControllerFactory();

    LOG.info("Building and starting WebServer");

    // Build the HTTP routes and start WebServer
    WebServer
        .builder()
        .addFeature(
            AccessLogFeature.builder()
                .commonLogFormat()
                .build())
        .mediaContext(it -> it
            .mediaSupportsDiscoverServices(false)
            .addMediaSupport(
                JacksonSupport.create(Serdes.INSTANCE.getObjectMapper()))
            .build())
        .routing(
            HttpRouting
                .builder()
                .register(CorsSupport.builder()
                    .allowOrigins("*")
                    .allowMethods("*")
                    .allowHeaders("*")
                    .build())
                .register("/configuration",
                    factory.createController(ControllerType.Configuration))
                .register("/analytics",
                    factory.createController(ControllerType.Analytics))
                .register("/activity",
                    factory.createController(ControllerType.Activity))
        )
        .config(config.get("server"))
        .build()
        .start();
  }
}
