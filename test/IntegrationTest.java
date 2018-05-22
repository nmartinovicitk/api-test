import org.junit.Test;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WSTestClient;
import play.test.WithServer;

import java.util.concurrent.CompletionStage;
import static play.test.Helpers.*;
import static org.junit.Assert.*;

/**
 * Integration testing that involves starting up an application or a server.
 * <p>
 * https://www.playframework.com/documentation/2.5.x/JavaFunctionalTest
 */
public class IntegrationTest extends WithServer {

    @Test
    public void testInServerThroughUrl() throws Exception {
        // Tests using a scoped WSClient to talk to the server through a port.
        try (WSClient ws = WSTestClient.newClient(this.testServer.port())) {
            CompletionStage<WSResponse> stage = ws.url("/persons").get();
            WSResponse response = stage.toCompletableFuture().get();
            int status = response.getStatus();

            assertTrue(status == OK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInServerThroughApp() throws Exception {
        // Tests using the internal application available in the server.
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/persons");

        // XXX This should be play.test.CSRFTokenHelper
        Http.RequestBuilder tokenRequest = play.api.test.CSRFTokenHelper.addCSRFToken(request);

        Result result = route(app, tokenRequest);
        int status = result.status();
        assertTrue(status == OK);
    }

}
