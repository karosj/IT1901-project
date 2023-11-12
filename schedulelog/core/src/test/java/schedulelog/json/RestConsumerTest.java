package schedulelog.json;

import schedulelog.core.Activity;
import schedulelog.core.Courses;
import schedulelog.core.Subject;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;


public class RestConsumerTest {

    private RestConsumer restConsumer = new RestConsumer();

    @Test
    public void testGetActivities() {
        System.out.println("testGetActivities start.");

        List<Activity> activities = restConsumer.getActivities();
        if (activities == null || activities.isEmpty()) {
            throw new AssertionError("No activities returned or an error occurred.");
        }
        System.out.println("activities:");
        System.out.println(activities);
        System.out.println("testGetActivities passed.");
    }

    @Test
    public void testAddActivity() {
        System.out.println("testAddActivity start.");

        List<Subject> subjects = Collections.singletonList(new Subject("TDT4120", new Courses()));
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);
        Activity activity = new Activity(subjects, startTime, endTime, "Study session");

        System.out.println("test to add:");
        System.out.println(activity);

        restConsumer.addActivity(activity);

        System.out.println("added now to add");
        // Verifying if the activity was added would require another GET request to
        // check
        // if the new activity is present, assuming the GET method returns the latest
        // data.
        List<Activity> activitiesAfterAdd = restConsumer.getActivities();
        System.out.println(activitiesAfterAdd.toString());
        if (activitiesAfterAdd == null || !activitiesAfterAdd.toString().contains(activity.toString())) {
            throw new AssertionError("Activity was not added successfully or an error occurred.");
        }
        System.out.println("testAddActivity passed.");
    }

}
