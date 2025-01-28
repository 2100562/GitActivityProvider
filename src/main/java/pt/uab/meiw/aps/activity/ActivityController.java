package pt.uab.meiw.aps.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.helidon.http.HeaderNames;
import io.helidon.http.Method;
import io.helidon.webserver.http.Handler;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.uab.meiw.aps.Constants;
import pt.uab.meiw.aps.Controller;
import pt.uab.meiw.aps.Serdes;

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

  private final ActivityService activityService;

  public ActivityController(ActivityService activityService) {
    this.activityService = activityService;
  }

  /**
   * Invoked by Helidon Webserver to configure the REST routes.
   *
   * @param httpRules the rules object to configure REST routes.
   */
  @Override
  public void routing(HttpRules httpRules) {
    final var activityDeploy = new ActivityDeployHandler(activityService);
    final var activityProvideGet = new ActivityProvideGetHandler(
        activityService);
    final var activityProvidePost = new ActivityProvidePostHandler(
        activityService);

    httpRules
        .get("/deploy/{id}", activityDeploy)
        .post("/deploy/{id}", activityDeploy)
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

    private final ObjectMapper mapper = Serdes.INSTANCE.getObjectMapper();
    private final ActivityService activityService;

    private ActivityDeployHandler(ActivityService activityService) {
      this.activityService = activityService;
    }

    @Override
    public void handle(ServerRequest req, ServerResponse res) throws Exception {
      if (req.prologue().method().equals(Method.GET)) {
        handleGet(req, res);
      } else {
        handlePost(req, res);
      }
    }

    public void handleGet(ServerRequest req, ServerResponse res) {
      final var deployUrl = getClientUrl(req);

      res.status(200).header("Content-Type", Constants.CONTENT_TYPE_JSON)
          .send(Collections.singletonMap("deployURL", deployUrl));
    }

    public String getClientUrl(ServerRequest req) {
      String forwardedProto = req.headers().value(HeaderNames.X_FORWARDED_PROTO)
          .orElse("http");
      String host = req.headers().value(HeaderNames.HOST).orElse("localhost");
      String path = req.requestedUri().path().toString();

      return forwardedProto + "://" + host + path;
    }

    public void handlePost(ServerRequest req, ServerResponse res)
        throws Exception {
      DeployRequest request;

      try {
        request = req.content().as(DeployRequest.class);
        LOG.info("Received deploy request: {}", request);
      } catch (RuntimeException e) {
        res.status(400).send();
        return;
      }

      final var activity = activityService.createFor(request);
      final var activityEncoded = new String(Base64.getEncoder().encode(
          mapper.writeValueAsString(activity).getBytes(StandardCharsets.UTF_8)),
          StandardCharsets.UTF_8);

      // Find redirect URL
      final var orig = req.requestedUri().toUri();
      final var query = "id=" + activity.getId() + "&data=" + activityEncoded;

      final var url = new URI(orig.getScheme(), orig.getAuthority(),
          "/activity", query, null).toURL().toString();

      res
          .status(200)
          .header("Content-Type", Constants.CONTENT_TYPE_JSON)
          .send(Collections.singletonMap("deployURL", url));
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

    private final ActivityService activityService;

    private ActivityProvideGetHandler(ActivityService activityService) {
      this.activityService = activityService;
    }

    @Override
    public void handle(ServerRequest req, ServerResponse res) throws Exception {
      if (!req.query().contains("id") || !req.query().contains("data")) {
        res.status(400).header("Content-Type", Constants.CONTENT_TYPE_HTML)
            .send("Missing Activity details");
      } else {
        res.status(200).header("Content-Type", Constants.CONTENT_TYPE_HTML)
            .send(activityService.getActivityInterface());
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

    private final ActivityService activityService;

    private ActivityProvidePostHandler(ActivityService activityService) {
      this.activityService = activityService;
    }

    @Override
    public void handle(ServerRequest req, ServerResponse res) throws Exception {

      ProvideRequest request;
      int status;

      try {
        request = req.content().as(ProvideRequest.class);
        status = 200;
        LOG.info("ProvideRequest: {}", request);
        activityService.setRepository(request.getActivityId(),
            request.getGitRepositoryUrl());
      } catch (RuntimeException e) {
        LOG.info("ProvideRequest failed", e);
        status = 400;
      }

      res.status(status).header("Content-Type", Constants.CONTENT_TYPE_JSON)
          .send();
    }
  }
}
