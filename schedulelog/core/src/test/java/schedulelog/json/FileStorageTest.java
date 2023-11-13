package schedulelog.json;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import schedulelog.core.Activity;
import schedulelog.core.Subject;
import schedulelog.core.Courses;

import java.time.LocalDateTime;
import java.util.Arrays;

public class FileStorageTest {

    private FileStorage storage;
    private Courses courses;
    private final String FILE_NAME = "test.json";

    @BeforeEach
    public void setUp() {
        this.storage = new FileStorage(FILE_NAME);
        this.courses = new Courses();
    }

    @Test
    public void testFileStorage() {
        // Create a sample activity
        Subject subject = new Subject("TMA4240", courses);
        LocalDateTime startTime = LocalDateTime.of(2023, 10, 30, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 10, 30, 12, 0);
        Activity activity = new Activity(Arrays.asList(subject), startTime, endTime, "Math class");

        // Add the activity to the storage
        storage.addActivity(activity);

        // Retrieve the activities from the storage
        FileStorage anotherStorage = new FileStorage(FILE_NAME);
        Activity retrievedActivity = anotherStorage.getActivities().get(0);

        // Assert that the added and retrieved activities are the same
        Assertions.assertEquals(activity.toString(), retrievedActivity.toString());
    }

    @Test
    public void testFileStorageJSON() {

        // Create a sample activity
        Subject subject = new Subject("TMA4240", courses);
        LocalDateTime startTime = LocalDateTime.of(2023, 10, 30, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 10, 30, 12, 0);
        Activity activity = new Activity(Arrays.asList(subject), startTime, endTime, "Math class");

        // Add the activity to the storage
        storage.addActivity(activity);

        // Retrieve the activities from the storage
        storage.getActivitiesJSON();

        // Assertions.assertTrue(activitiesJSON.contains("{\"code\":\"TMA4240\",\"name\":\"Statistikk\"},\"startTime\":\"2023-10-30T10:00:00\",\"endTime\":\"2023-10-30T12:00:00\",\"description\":\"Math class\""));
    }

    
}