package pt.uab.meiw.aps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.helidon.config.Config;
import io.helidon.http.media.jackson.JacksonSupport;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.accesslog.AccessLogFeature;
import io.helidon.webserver.http.HttpRouting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.uab.meiw.aps.configuration.ConfigurationController;
import pt.uab.meiw.aps.configuration.impl.ConfigurationServiceImpl;

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

    final var om = new ObjectMapper();

    final var configService = new ConfigurationServiceImpl(om, config);
    final var configController = new ConfigurationController(configService);

    // For part 1 we're only providing the basic REST APIs
    LOG.info("Building and starting Webserver");

    // Build the HTTP routes and start WebServer
    WebServer
        .builder()
        .addFeature(
            AccessLogFeature.builder()
                .commonLogFormat()
                .build())
        .mediaContext(it -> it
            .mediaSupportsDiscoverServices(false)
            .addMediaSupport(JacksonSupport.create(om))
            .build())
        .routing(
            HttpRouting
                .builder()
                .register("/configuration", configController)
        )
        .config(config.get("server"))
        .build()
        .start();
  }
}
