import org.junit.Test;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.test.WSTestClient;
import play.test.WithServer;

import java.util.concurrent.CompletionStage;
import static play.test.Helpers.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

/**
 * Integration testing that involves starting up an application or a server.
 * <p>
 * https://www.playframework.com/documentation/2.5.x/JavaFunctionalTest
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AcceptanceTest extends WithServer {

    private int postPerson (String name) throws Exception {
        int status = 0;

        try (WSClient ws = WSTestClient.newClient(this.testServer.port())) {
            CompletionStage<WSResponse> stage = ws.url("/person").setContentType("application/x-www-form-urlencoded")
                    .post("name=" + name);
            WSResponse response = stage.toCompletableFuture().get();
            status = response.getStatus();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return status;
    }

    @Test
    public void testAddingPerson() throws Exception {
        int status = postPerson("Alice");
        assertTrue(status == OK);
    }

    @Test
    public void testAddingSecondPerson() throws Exception {
        int status = postPerson("Bob");
        assertTrue(status == OK);
    }

    @Test
    public void testGetPersons() throws Exception {
        // Tests using a scoped WSClient to talk to the server through a port.
        try (WSClient ws = WSTestClient.newClient(this.testServer.port())) {
            CompletionStage<WSResponse> stage = ws.url("/persons").get();
            WSResponse response = stage.toCompletableFuture().get();
            String body = response.getBody();

            assertThat(body, containsString("Alice"));
            assertThat(body, containsString("Bob"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

