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
public class ActivityAnalyticsMapper implements DbMapper<ActivityAnalytics> {

  @SuppressWarnings("unchecked")
  @Override
  public ActivityAnalytics read(DbRow row) {
    final var aa = new ActivityAnalytics();

    aa.setInveniraStdID(row.column("inveniraStdID").asString().get());
    aa.setQualitativeAnalytics(
        row.column("qualitativeAnalytics").as(List.class).get());
    aa.setQuantitativeAnalytics(
        row.column("quantitativeAnalytics").as(List.class).get());
    aa.setExternalActivityId(row.column("externalActivityId").asString().get());

    return aa;
  }

  @Override
  public Map<String, ?> toNamedParameters(ActivityAnalytics value) {
    final var map = new HashMap<String, Object>();

    map.put("inveniraStdID", value.getInveniraStdID());
    map.put("qualitativeAnalytics", value.getQualitativeAnalytics());
    map.put("quantitativeAnalytics", value.getQuantitativeAnalytics());
    map.put("externalActivityId", value.getExternalActivityId());

    return map;
  }

  @Override
  public List<?> toIndexedParameters(ActivityAnalytics value) {
    final var list = new LinkedList<>();

    list.add(value.getInveniraStdID());
    list.add(value.getQualitativeAnalytics());
    list.add(value.getQuantitativeAnalytics());
    list.add(value.getExternalActivityId());

    return list;
  }
}
