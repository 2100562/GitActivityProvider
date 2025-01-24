package pt.uab.meiw.aps.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import io.helidon.config.Config;
import io.helidon.webserver.http.HttpRules;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import pt.uab.meiw.aps.Constants;
import pt.uab.meiw.aps.Controller;
import pt.uab.meiw.aps.DeserializationException;
import pt.uab.meiw.aps.Serdes;
import pt.uab.meiw.aps.Utils;

/**
 * The Configuration Controller is responsible for configuring the Helidon
 * Webserver routes to handle the Configuration Parameters and the Configuration
 * Interface requests.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ConfigurationController implements Controller {

  private static final String DESER_ERR = "Exception while deserializing configuration parameters";

  private final List<Map<String, Object>> configurationParameters;
  private final String configurationInterface;

  public ConfigurationController() {
    final var om = Serdes.INSTANCE.getObjectMapper();
    final var config = Config.global();

    final var configParPath = config
        .get("ap")
        .get("configuration")
        .get("parameters-path")
        .asString()
        .orElse("");

    final var configHtmlPath = config
        .get("ap")
        .get("configuration")
        .get("interface-path")
        .asString()
        .orElse("");

    final var configParFile = Utils.readFile(configParPath);
    final var configHtmlFile = Utils.readFile(configHtmlPath);

    if (configParFile.isEmpty()) {
      configurationParameters = new LinkedList<>();
    } else {
      final var type = new TypeReference<List<Map<String, Object>>>() {
      };

      try {
        configurationParameters = om.readValue(configParFile.get(), type);
      } catch (IOException e) {
        throw new DeserializationException(DESER_ERR, e);
      }
    }

    configurationInterface = configHtmlFile
        .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
        .orElse("");
  }

  /**
   * Invoked by Helidon Webserver to configure the REST routes.
   *
   * @param httpRules the rules object to configure REST routes.
   */
  @Override
  public void routing(HttpRules httpRules) {
    httpRules
        .get("/parameters", (req, res) -> {
          res
              .status(200)
              .header("Content-Type", Constants.CONTENT_TYPE_JSON)
              .send(Collections.unmodifiableList(configurationParameters));
        })
        .get("/interface", (req, res) -> {
          res
              .status(200)
              .header("Content-Type", Constants.CONTENT_TYPE_JSON)
              .send(configurationInterface);
        });
  }
}
