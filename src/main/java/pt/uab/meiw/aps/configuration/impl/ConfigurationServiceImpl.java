package pt.uab.meiw.aps.configuration.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.helidon.config.Config;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import pt.uab.meiw.aps.DeserializationException;
import pt.uab.meiw.aps.Utils;
import pt.uab.meiw.aps.configuration.ConfigurationService;

/**
 * The Configuration Service implementation which loads both the configuration
 * parameters and configuration interface from the specified files in the
 * configuration, if no configuration or the files cannot be read, empty values
 * are used.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public class ConfigurationServiceImpl implements ConfigurationService {

  private static final String DESER_ERR = "Exception while deserializing configuration parameters";

  private final List<Map<String, Object>> configurationParameters;
  private final String configurationInterface;

  public ConfigurationServiceImpl(ObjectMapper om, Config config) {
    Objects.requireNonNull(om, "Object Mapper must not be null");
    Objects.requireNonNull(config, "Config must not be null");

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

  @Override
  public List<Map<String, Object>> getConfigurationParameters() {
    return Collections.unmodifiableList(configurationParameters);
  }

  @Override
  public String getConfigurationInterface() {
    return configurationInterface;
  }
}
