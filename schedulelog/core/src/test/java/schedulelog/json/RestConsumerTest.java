package schedulelog.json;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import schedulelog.core.Activity;
import schedulelog.core.Courses;
import schedulelog.core.Subject;

/**
 * Test class for FileStorage.
 */
public class RestConsumerTest {

    private WireMockServer wireMockServer;
    private RestConsumer restConsumer;

    // Sets up the test environment, including a mock server and RestConsumer
    // instance.
    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);
        restConsumer = new RestConsumer();
    }

    // Tears down the test environment, stopping the mock server.
    @AfterEach
    public void teardown() {
        wireMockServer.stop();
    }

    // Tests the getActivities method of RestConsumer to ensure it correctly fetches
    // activities.
    @Test
    public void testGetActivities() {
        String jsonResponse = "[{\"subjects\":[{\"code\":\"TMA4240\",\"name\":\"Statistikk\"}],\"startTime\":\"2023-10-30T10:00:00\",\"endTime\":\"2023-10-30T12:00:00\",\"description\":\"Math class\"}]";
        stubFor(get(urlEqualTo("/activities"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));
        System.out.println("testGetActivities start.");

        List<Activity> activities = restConsumer.getActivities();
        if (activities == null || activities.isEmpty()) {
            throw new AssertionError("No activities returned or an error occurred.");
        }
        System.out.println("activities:");
        System.out.println(activities);
        System.out.println("testGetActivities passed.");
    }

    // Tests the addActivity method of RestConsumer to ensure it correctly adds an
    // activity.
    @Test
    public void testAddActivity() {
        System.out.println("testAddActivity start.");
        stubFor(post(urlEqualTo("/addActivity"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Mock response activity added successfully"))); // Mock response body

        List<Subject> subjects = Collections.singletonList(new Subject("TDT4120", new Courses()));
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);
        Activity activity = new Activity(subjects, startTime, endTime, "Study session");

        System.out.println("test to add:");
        System.out.println(activity);

        String result = restConsumer.addActivity(activity);

        System.out.println("result:");
        System.out.println(result);

        if (result == null || !result.contains("Mock response activity added successfully")) {
            throw new AssertionError("Activity was not added successfully or an error occurred.");
        }
        System.out.println("testAddActivity passed.");
    }

}
