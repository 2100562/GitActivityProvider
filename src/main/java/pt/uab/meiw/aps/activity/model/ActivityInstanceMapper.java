package pt.uab.meiw.aps.activity.model;

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
public class ActivityInstanceMapper implements DbMapper<ActivityInstance> {

  @Override
  public ActivityInstance read(DbRow dbRow) {
    final var aI = new ActivityInstance();

    aI.setId(dbRow.column("_id").toString());
    aI.setActivityName(dbRow.column("activityName").toString());
    aI.setActivityInstructions(dbRow.column("activityInstructions").toString());
    aI.setExternalActivityId(dbRow.column("externalActivityId").toString());
    aI.setExternalStudentId(dbRow.column("externalStudentId").toString());
    aI.setRepositoryUrl(dbRow.column("repositoryUrl").toString());

    return aI;
  }

  @Override
  public Map<String, ?> toNamedParameters(ActivityInstance activityInstance) {
    final var map = new HashMap<String, Object>();

    map.put("id", activityInstance.getId());
    map.put("activityName", activityInstance.getActivityName());
    map.put("activityInstructions", activityInstance.getActivityInstructions());
    map.put("externalActivityId", activityInstance.getExternalActivityId());
    map.put("externalStudentId", activityInstance.getExternalStudentId());
    map.put("repositoryUrl", activityInstance.getRepositoryUrl());

    return map;
  }

  @Override
  public List<?> toIndexedParameters(ActivityInstance activityInstance) {
    final var list = new LinkedList<>();

    list.add(activityInstance.getId());
    list.add(activityInstance.getActivityName());
    list.add(activityInstance.getActivityInstructions());
    list.add(activityInstance.getExternalActivityId());
    list.add(activityInstance.getExternalStudentId());
    list.add(activityInstance.getRepositoryUrl());

    return list;
  }
}
