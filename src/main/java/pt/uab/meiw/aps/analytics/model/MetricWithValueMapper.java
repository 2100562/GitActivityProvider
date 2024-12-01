package pt.uab.meiw.aps.analytics.model;

import io.helidon.dbclient.DbMapper;
import io.helidon.dbclient.DbRow;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    final var map = new HashMap<String, Object>();

    map.put("name", value.getName());
    map.put("type", value.getType());
    map.put("value", value.getValue());

    return map;
  }

  @Override
  public List<?> toIndexedParameters(MetricWithValue value) {
    final var list = new LinkedList<>();

    list.add(value.getName());
    list.add(value.getType());
    list.add(value.getValue());

    return list;
  }
}
