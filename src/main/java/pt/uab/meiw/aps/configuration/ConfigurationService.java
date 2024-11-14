package pt.uab.meiw.aps.configuration;

import java.util.List;
import java.util.Map;

/**
 * A Configuration Service must provide two methods, one that returns the
 * Configuration Parameters and another that returns the Configuration Interface
 * as specified in the Inven!RA Platform.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public interface ConfigurationService {

  /**
   * Returns the Configuration Parameters.
   *
   * @return a {@link List} of parameters.
   */
  List<Map<String, Object>> getConfigurationParameters();

  /**
   * Returns the HTML for the Configuration Interface.
   *
   * @return an HTML {@link String}
   */
  String getConfigurationInterface();
}
