package pt.uab.meiw.aps.analytics.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.helidon.config.Config;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import pt.uab.meiw.aps.DeserializationException;
import pt.uab.meiw.aps.Utils;
import pt.uab.meiw.aps.analytics.AnalyticsContract;
import pt.uab.meiw.aps.analytics.AnalyticsService;

/**
 * The Analytics Service implementation, which loads the analytics contract from
 * the specified file in the configuration, if no configuration or the file
 * cannot be read, empty values are used.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class AnalyticsServiceImpl implements AnalyticsService {

  private static final String DESER_ERR = "Exception while deserializing configuration parameters";

  private final AnalyticsContract analyticsContract;

  public AnalyticsServiceImpl(ObjectMapper om, Config config) {
    Objects.requireNonNull(om, "Object Mapper must not be null");
    Objects.requireNonNull(config, "Config must not be null");

    final var analyticsContractPath = config
        .get("ap")
        .get("analytics")
        .get("contract-path")
        .asString()
        .orElse("");

    final var analyticsContractFile = Utils.readFile(analyticsContractPath);

    if (analyticsContractFile.isEmpty()) {
      analyticsContract = new AnalyticsContract();
    } else {
      try {
        analyticsContract = om.readValue(analyticsContractFile.get(),
            AnalyticsContract.class);
      } catch (IOException e) {
        throw new DeserializationException(DESER_ERR, e);
      }
    }
  }

  @Override
  public AnalyticsContract getAnalyticsContract() {
    return analyticsContract;
  }

  @Override
  public Object getAnalytics() {
    return Collections.emptyList();
  }
}
