package pt.uab.meiw.aps.configuration;

import io.helidon.webserver.http.Handler;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import pt.uab.meiw.aps.Constants;
import pt.uab.meiw.aps.Controller;

/**
 * The Configuration Controller is responsible for configuring the Helidon
 * Webserver routes to handle the Configuration Parameters and the Configuration
 * Interface requests.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ConfigurationController implements Controller {

  private final ConfigurationService configurationService;

  public ConfigurationController(ConfigurationService configurationService) {
    this.configurationService = configurationService;
  }

  /**
   * Invoked by Helidon Webserver to configure the REST routes.
   *
   * @param httpRules the rules object to configure REST routes.
   */
  @Override
  public void routing(HttpRules httpRules) {
    final var configParametersHandler = new ConfigurationParametersHandler(
        configurationService);

    final var configInterfaceHandler = new ConfigurationInterfaceHandler(
        configurationService);

    httpRules
        .get("/parameters", configParametersHandler)
        .get("/interface", configInterfaceHandler);
  }

  /**
   * The Configuration Parameters Handler which handles requests to
   * /configuration/parameters.
   *
   * @author Hugo Gonçalves
   * @since 0.0.1
   */
  private static final class ConfigurationParametersHandler implements Handler {

    private final ConfigurationService configurationService;

    private ConfigurationParametersHandler(
        ConfigurationService configurationService) {
      this.configurationService = configurationService;
    }

    @Override
    public void handle(ServerRequest req, ServerResponse res) throws Exception {
      res
          .status(200)
          .header("Content-Type", Constants.CONTENT_TYPE_JSON)
          .send(configurationService.getConfigurationParameters());
    }
  }

  /**
   * The Configuration Interface Handler which handles requests to
   * /configuration/interface.
   *
   * @author Hugo Gonçalves
   * @since 0.0.1
   */
  private static final class ConfigurationInterfaceHandler implements Handler {

    private final ConfigurationService configurationService;

    private ConfigurationInterfaceHandler(
        ConfigurationService configurationService) {
      this.configurationService = configurationService;
    }

    @Override
    public void handle(ServerRequest req, ServerResponse res) throws Exception {
      res
          .status(200)
          .header("Content-Type", Constants.CONTENT_TYPE_HTML)
          .send(configurationService.getConfigurationInterface());
    }
  }
}
