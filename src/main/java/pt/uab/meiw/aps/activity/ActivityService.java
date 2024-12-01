package pt.uab.meiw.aps.activity;

import pt.uab.meiw.aps.Service;
import pt.uab.meiw.aps.activity.model.ActivityInstance;

/**
 * The Activity Service Interface providing methods to create new Activity
 * Instances.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public interface ActivityService extends Service {

  /**
   * Create a new Activity Instance from the Inven!RA Deploy Request.
   *
   * @param deployRequest the deployment request.
   * @return the created Activity Instance.
   */
  ActivityInstance createFor(DeployRequest deployRequest);

  /**
   * Sets the repository URL for an already create Activity Instance.
   *
   * @param id            the Activity Instance id.
   * @param repositoryUrl the repository URL to set.
   * @return the updated Activity Instance.
   */
  ActivityInstance setRepository(
      String id,
      String repositoryUrl
  );

  /**
   * Attempt find an Activity Instance by id.
   *
   * @param id the id to find.
   * @return the Activity Instance of null.
   */
  ActivityInstance getActivity(
      String id
  );

  /**
   * Returns the Activity Interface HTML Page.
   *
   * @return the HTML page.
   */
  String getActivityInterface();
}
