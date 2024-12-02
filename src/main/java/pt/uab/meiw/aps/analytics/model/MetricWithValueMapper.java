package pt.uab.meiw.aps.analytics.model;

import io.helidon.dbclient.DbMapper;
import io.helidon.dbclient.DbRow;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bson.Document;

/**
 * A Helidon DBClient Model Mapper.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public class MetricWithValueMapper implements DbMapper<MetricWithValue<?>> {

  @Override
  public MetricWithValue<?> read(DbRow row) {
    final var metric = new MetricWithValue<>();

    metric.setName(row.column("name").asString().get());
    metric.setType(row.column("type").asString().get());
    metric.setValue(row.column("value").get());

    return metric;
  }

  @Override
  public Map<String, ?> toNamedParameters(MetricWithValue value) {
    return null;
  }

  @Override
  public List<?> toIndexedParameters(MetricWithValue value) {
    return null;
  }
}
