package schedulelog.json;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import schedulelog.core.Activity;
import schedulelog.core.Subject;
import schedulelog.core.Courses;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class FileStorageTest {

    private FileStorage storage;
    private Courses courses;
    private final String FILE_NAME = "test.json";

    @BeforeEach
    public void setUp() throws IOException {
        Files.deleteIfExists(Paths.get(FILE_NAME));
        this.storage = new FileStorage(FILE_NAME);
        this.courses = new Courses();
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Clean up by deleting the test file after each test
        Files.deleteIfExists(Paths.get(FILE_NAME));
    }

    @Test
    public void testEmptyConstructor() {
        FileStorage fileStorage = new FileStorage();
        assertTrue(fileStorage.getFileName() == "activities.json");
    }

    @Test
    public void testFileStorageExistingFile() throws IOException {

        File file = new File(FILE_NAME);
        file.createNewFile();
        new ObjectMapper().writeValue(file, "[{\"subjects\":[{\"code\":\"TMA4240\",\"name\":\"Statistikk\"}],\"startTime\":\"2023-10-30T10:00:00\",\"endTime\":\"2023-10-30T12:00:00\",\"description\":\"Math class\"}]");

        // Create a sample activity
        Subject subject = new Subject("TMA4240", courses);
        LocalDateTime startTime = LocalDateTime.of(2023, 10, 30, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 10, 30, 12, 0);
        Activity activity = new Activity(Arrays.asList(subject), startTime, endTime, "Math class");

        // Add the activity to the storage
        storage.addActivity(activity);

        // Delete file again
        Files.deleteIfExists(Paths.get(FILE_NAME));

        // Create a new storage with the same file name
        FileStorage anotherStorage = new FileStorage(FILE_NAME);

        // Assert that the added and retrieved activities are the same
        Assertions.assertEquals(new ArrayList<Activity>(), anotherStorage.getActivities());
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
    public void testUnreadableFile() throws IOException {
        File file = new File(FILE_NAME);
        file.createNewFile();
        file.setReadable(false);
        file.setWritable(false);

        // Create a sample activity
        Subject subject = new Subject("TMA4240", courses);
        LocalDateTime startTime = LocalDateTime.of(2023, 10, 30, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 10, 30, 12, 0);
        Activity activity = new Activity(Arrays.asList(subject), startTime, endTime, "Math class");

        // Add the activity to the storage
        storage.addActivity(activity);

        String jsonResult = storage.getActivitiesJSON();
        System.out.println("jsonResult:");
        System.out.println(jsonResult);
        
        // Since the method catches the IOException, we just ensure it doesn't throw an unhandled exception
        assertTrue(file.exists());

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