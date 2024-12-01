package pt.uab.meiw.aps.activity.model;

import io.helidon.dbclient.DbMapper;
import io.helidon.dbclient.spi.DbMapperProvider;
import java.util.Optional;

/**
 * Provider for {@link ActivityInstanceMapper}.
 *
 * @author HUgo Gonçalves
 * @since 0.0.1
 */
public final class ActivityInstanceMapperProvider implements DbMapperProvider {

  private static final ActivityInstanceMapper INSTANCE = new ActivityInstanceMapper();

  @SuppressWarnings("unchecked")
  @Override
  public <T> Optional<DbMapper<T>> mapper(Class<T> clazz) {
    if (clazz.equals(ActivityInstance.class)) {
      return Optional.of((DbMapper<T>) INSTANCE);
    }
    return Optional.empty();
  }
}
