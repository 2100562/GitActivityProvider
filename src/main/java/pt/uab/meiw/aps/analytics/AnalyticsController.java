package pt.uab.meiw.aps.analytics;

import io.helidon.webserver.http.Handler;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import pt.uab.meiw.aps.AnalyticsRequest;
import pt.uab.meiw.aps.Constants;
import pt.uab.meiw.aps.Controller;

/**
 * The Analytics Controller is responsible for configuring the Helidon Webserver
 * routes to handle the Analytics Contract and the Provide Analytics requests.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class AnalyticsController implements Controller {

  private final AnalyticsService analyticsService;

  public AnalyticsController(AnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
  }

  /**
   * Invoked by Helidon Webserver to configure the REST routes.
   *
   * @param httpRules the rules object to configure REST routes.
   */
  @Override
  public void routing(HttpRules httpRules) {
    final var analyticsContractHandler = new AnalyticsContractHandler(
        analyticsService);

    final var analyticsHandler = new AnalyticsHandler(analyticsService);

    httpRules
        .post("/", analyticsHandler)
        .get("/contract", analyticsContractHandler);
  }

  /**
   * The Analytics Contract Handler which handles requests to
   * /analytics/contract.
   *
   * @author Hugo Gonçalves
   * @since 0.0.1
   */
  private static final class AnalyticsContractHandler implements Handler {

    private final AnalyticsService analyticsService;

    public AnalyticsContractHandler(AnalyticsService analyticsService) {
      this.analyticsService = analyticsService;
    }

    @Override
    public void handle(ServerRequest req, ServerResponse res) throws Exception {
      res
          .status(200)
          .header("Content-Type", Constants.CONTENT_TYPE_JSON)
          .send(analyticsService.getAnalyticsContract());
    }
  }

  /**
   * The Analytics Handler which handles requests to /analytics.
   *
   * @author Hugo Gonçalves
   * @since 0.0.1
   */
  private static final class AnalyticsHandler implements Handler {

    private final AnalyticsService analyticsService;

    public AnalyticsHandler(AnalyticsService analyticsService) {
      this.analyticsService = analyticsService;
    }

    @Override
    public void handle(ServerRequest req, ServerResponse res) throws Exception {

      AnalyticsRequest request;

      try {
        request = req.content().as(AnalyticsRequest.class);
      } catch (Exception e) {
        res.status(400).send();
        return;
      }

      res
          .status(200)
          .header("Content-Type", Constants.CONTENT_TYPE_JSON)
          .send(analyticsService.getAnalytics(request.getActivityId()));
    }
  }
}
