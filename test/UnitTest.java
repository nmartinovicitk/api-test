import controllers.PersonController;
import models.Person;
import models.PersonRepository;
import org.junit.Test;
import play.core.j.JavaContextComponents;
import play.core.j.JavaHelpers$;
import play.data.FormFactory;
import play.data.format.Formatters;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import javax.validation.Validator;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ForkJoinPool;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.OK;

/**
 * Simple (JUnit) tests that can call all parts of a play app.
 * <p>
 * https://www.playframework.com/documentation/latest/JavaTest
 */
public class UnitTest {

    private JavaContextComponents createContextComponents() {
        return JavaHelpers$.MODULE$.createContextComponents();
    }

    @Test
    public void checkAddPerson() {
        // Easier to mock out the form factory inputs here
        MessagesApi messagesApi = mock(MessagesApi.class);
        Validator validator = mock(Validator.class);
        FormFactory formFactory = new FormFactory(messagesApi, new Formatters(messagesApi), validator);

        // Don't need to be this involved in setting up the mock, but for demo it works:
        PersonRepository repository = mock(PersonRepository.class);
        Person person = new Person();
        person.id = 1L;
        person.name = "Steve";
        when(repository.add(any())).thenReturn(supplyAsync(() -> person));

        // Set up the request builder to reflect input
        final Http.RequestBuilder requestBuilder = new Http.RequestBuilder().method("post").bodyJson(Json.toJson(person));

        // Add in an Http.Context here using invokeWithContext:
        // XXX extending JavaHelpers is a cheat to get at JavaContextComponents easily, put this into helpers
        JavaContextComponents components = createContextComponents();
        final CompletionStage<Result> stage = Helpers.invokeWithContext(requestBuilder, components, () -> {
            HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());

            // Create controller and call method under test:
            final PersonController controller = new PersonController(formFactory, repository, ec);
            return controller.addPerson();
        });

        // Test the completed result
        await().atMost(1, SECONDS).until(() ->
            assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(result ->
                result.status() == OK, "Should return OK after operation"
            )
        );
    }

}
