package schedulelog.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityTest {

    // Reference to the Subject class for testing.
    private Subject testSubject;
    
    // Activity instance for testing purposes.
    private Activity activity;

    // Setting up the test environment before each test.
    @BeforeEach
    public void setup() {
        testSubject = new Subject();
        List<String> subjectCodes = Arrays.asList("TMA4240", "TDT4160");
        LocalDateTime start = LocalDateTime.of(2023, 10, 10, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 10, 12, 0);
        activity = new Activity(subjectCodes, start, end, "Aktivitet", testSubject);
    }

    // Testing the getSubjectNames method.
    @Test
    public void testGetSubjectNames() {
        List<String> names = activity.getSubjectNames();
        assertTrue(names.contains("Statistikk"));
        assertTrue(names.contains("Datamaskiner og digitalteknikk"));
    }

    // Testing the calculateDuration method.
    @Test
    public void testCalculateDuration() {
        int duration = activity.calculateDuration();
        assertEquals(120, duration);  // 120 minutes or 2 hours
    }

    // Testing the getter and setter for the start time.
    @Test
    public void testGetAndSetStartTime() {
        LocalDateTime newStart = LocalDateTime.of(2023, 10, 10, 9, 0);
        activity.setStartTime(newStart);
        assertEquals(newStart, activity.getStartTime());
    }

    // Testing the getter and setter for the end time.
    @Test
    public void testGetAndSetEndTime() {
        LocalDateTime newEnd = LocalDateTime.of(2023, 10, 10, 13, 0);
        activity.setEndTime(newEnd);
        assertEquals(newEnd, activity.getEndTime());
    }

    // Testing the getter and setter for the description.
    @Test
    public void testGetAndSetDescription() {
        String newDescription = "Oppdatert aktivitet";
        activity.setDescription(newDescription);
        assertEquals(newDescription, activity.getDescription());
    }

    // Testing the getter and setter for the subject codes.
    @Test
    public void testGetAndSetSubjectCodes() {
        List<String> newCodes = Arrays.asList("TDT4120", "IT1901");
        activity.setSubjectCodes(newCodes);
        assertTrue(activity.getSubjectCodes().contains("TDT4120"));
        assertTrue(activity.getSubjectCodes().contains("IT1901"));
    }

    // Testing the string representation of the Activity class.
    @Test
    public void testToString() {
        String representation = activity.toString();
        assertTrue(representation.contains("TMA4240"));
        assertTrue(representation.contains("Statistikk"));
        assertTrue(representation.contains("Aktivitet"));
    }
}
