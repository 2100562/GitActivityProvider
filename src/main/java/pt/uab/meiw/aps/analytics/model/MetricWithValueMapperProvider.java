package pt.uab.meiw.aps.analytics.model;

import io.helidon.dbclient.DbMapper;
import io.helidon.dbclient.spi.DbMapperProvider;
import java.util.Optional;

/**
 * Provider for {@link MetricWithValueMapper}.
 *
 * @author HUgo Gonçalves
 * @since 0.0.1
 */
public class MetricWithValueMapperProvider implements DbMapperProvider {

  private static final MetricWithValueMapper INSTANCE = new MetricWithValueMapper();

  @SuppressWarnings("unchecked")
  @Override
  public <T> Optional<DbMapper<T>> mapper(Class<T> clazz) {
    if (clazz.equals(MetricWithValue.class)) {
      return Optional.of((DbMapper<T>) INSTANCE);
    }
    return Optional.empty();
  }
}
