package schedulelog.json;

import schedulelog.core.Activity;
import schedulelog.core.Subject;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class RestConsumerTest {

    private RestConsumer restConsumer = new RestConsumer();

    public void testGetActivities() {
        List<Activity> activities = restConsumer.getActivities();
        if (activities == null || activities.isEmpty()) {
            throw new AssertionError("No activities returned or an error occurred.");
        }
        System.out.println("testGetActivities passed.");
    }

    public void testAddActivity() {
        List<Subject> subjects = Collections.singletonList(new Subject());
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);
        Activity activity = new Activity(subjects, startTime, endTime, "Study session");

        restConsumer.addActivity(activity);
        // Verifying if the activity was added would require another GET request to
        // check
        // if the new activity is present, assuming the GET method returns the latest
        // data.
        List<Activity> activitiesAfterAdd = restConsumer.getActivities();
        if (activitiesAfterAdd == null || !activitiesAfterAdd.contains(activity)) {
            throw new AssertionError("Activity was not added successfully or an error occurred.");
        }
        System.out.println("testAddActivity passed.");
    }

}
