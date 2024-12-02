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
public class ActivityAnalyticsMapper implements DbMapper<ActivityAnalytics> {

  private static final MetricWithValueMapper METRIC_MAPPER = new MetricWithValueMapper();

  @SuppressWarnings("unchecked")
  @Override
  public ActivityAnalytics read(DbRow row) {
    final var aa = new ActivityAnalytics();
    aa.setInveniraStdID(row.column("inveniraStdID").asString().get());
    aa.setQualitativeAnalytics(
        row.column("qualAnalytics").as(List.class).get().stream()
            .map(ActivityAnalyticsMapper::mapMetric).toList());
    aa.setQuantitativeAnalytics(
        row.column("quantAnalytics").as(List.class).get().stream()
            .map(ActivityAnalyticsMapper::mapMetric).toList());
    aa.setExternalActivityId(row.column("externalActivityId").asString().get());

    return aa;
  }

  @Override
  public Map<String, ?> toNamedParameters(ActivityAnalytics value) {
    return null;
  }

  @Override
  public List<?> toIndexedParameters(ActivityAnalytics value) {
    return null;
  }

  private static MetricWithValue<?> mapMetric(Document doc) {
    final var metric = new MetricWithValue<>();

    metric.setName(doc.getString("name"));
    metric.setType(doc.getString("type"));
    metric.setValue(doc.get("value"));

    return metric;
  }

  private static MetricWithValue<?> mapMetric(Object doc) {
    return mapMetric((Document) doc);
  }
}
