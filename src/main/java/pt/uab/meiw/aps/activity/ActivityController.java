package pt.uab.meiw.aps.activity;

import io.helidon.webserver.http.Handler;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.uab.meiw.aps.Constants;
import pt.uab.meiw.aps.Controller;

/**
 * The Activity Controller is responsible for configuring the Helidon Webserver
 * routes to handle the Deploy and the Provide Activity requests. Additionally,
 * there's another route to receive the activity configuration such as Git URL
 * from the Agent performing the Activity.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ActivityController implements Controller {

  private static final Logger LOG = LoggerFactory.getLogger(
      ActivityController.class);

  /**
   * Invoked by Helidon Webserver to configure the REST routes.
   *
   * @param httpRules the rules object to configure REST routes.
   */
  @Override
  public void routing(HttpRules httpRules) {
    final var activityDeploy = new ActivityDeployHandler();
    final var activityProvideGet = new ActivityProvideGetHandler();
    final var activityProvidePost = new ActivityProvidePostHandler();

    httpRules
        .post("/deploy", activityDeploy)
        .get("/", activityProvideGet)
        .post("/", activityProvidePost);
  }

  /**
   * The Activity Deploy Handler which handles requests to /activity/deploy.
   *
   * @author Hugo Gonçalves
   * @since 0.0.1
   */
  private static final class ActivityDeployHandler implements Handler {

    @Override
    public void handle(ServerRequest req, ServerResponse res) throws Exception {
      DeployRequest request = null;
      int status;

      try {
        request = req.content().as(DeployRequest.class);
        status = 200;
        LOG.info("Received deploy request: {}", request);
      } catch (RuntimeException e) {
        status = 400;
      }

      // Find redirect URL
      final var orig = req.requestedUri().toUri();
      String url = "";

      if (request != null) {
        final var id =
            "id=" + URLEncoder.encode(request.getActivityId(),
                StandardCharsets.UTF_8);
        url = new URI(
            orig.getScheme(),
            orig.getAuthority(),
            "/activity",
            id,
            null
        ).toURL().toString();
      }

      // MVP takes no actions, only acks the request.
      res.status(status).send(url);
    }
  }

  /**
   * The Activity Provide Get Handler which handles requests to /activity?id=##,
   * providing the activity interface for the student/trainee. A GET request
   * should contain an id query and returns the Activity Interface.
   *
   * @author Hugo Gonçalves
   * @since 0.0.1
   */
  private static final class ActivityProvideGetHandler implements Handler {

    @Override
    public void handle(ServerRequest req, ServerResponse res) throws Exception {
      if (!req.query().contains("id")) {
        res
            .status(400)
            .header("Content-Type", Constants.CONTENT_TYPE_HTML)
            .send("Missing Activity id");
      } else {
        res
            .status(200)
            .header("Content-Type", Constants.CONTENT_TYPE_HTML)
            .send("Git Activity id: " + req.query().get("id"));
      }
    }
  }

  /**
   * The Activity Provide Post Handler which handles requests to /activity,
   * request should contain the {@link ProvideRequest} payload.
   *
   * @author Hugo Gonçalves
   * @since 0.0.1
   */
  private static final class ActivityProvidePostHandler implements Handler {

    @Override
    public void handle(ServerRequest req, ServerResponse res) throws Exception {

      ProvideRequest request;
      int status;

      try {
        request = req.content().as(ProvideRequest.class);
        status = 200;
        LOG.info("ProvideRequest deploy request: {}", request);
      } catch (RuntimeException e) {
        status = 400;
      }

      res
          .status(status)
          .header("Content-Type", Constants.CONTENT_TYPE_JSON)
          .send();
    }
  }
}
