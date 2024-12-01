package pt.uab.meiw.aps.analytics.model;

import io.helidon.dbclient.DbMapper;
import io.helidon.dbclient.spi.DbMapperProvider;
import java.util.Optional;

/**
 * Provider for {@link ActivityAnalyticsMapper}.
 *
 * @author HUgo Gonçalves
 * @since 0.0.1
 */
public class ActivityAnalyticsMapperProvider implements DbMapperProvider {

  private static final ActivityAnalyticsMapper INSTANCE = new ActivityAnalyticsMapper();

  @SuppressWarnings("unchecked")
  @Override
  public <T> Optional<DbMapper<T>> mapper(Class<T> clazz) {
    if (clazz.equals(ActivityAnalytics.class)) {
      return Optional.of((DbMapper<T>) INSTANCE);
    }
    return Optional.empty();
  }
}
