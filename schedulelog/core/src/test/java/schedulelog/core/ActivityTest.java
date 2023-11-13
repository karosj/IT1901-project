package schedulelog.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityTest {

    private Activity activity;
    private Subject subject1;
    private Subject subject2;
    private Courses courses;

    @BeforeEach
    public void setUp() {
        List<String> codes = Arrays.asList("TDT4140", "TDT4242");
        List<String> names = Arrays.asList("Programvareutvikling", "Avansert programvareutvikling");
        courses = new Courses(codes, names);

        subject1 = new Subject("TDT4140", courses);
        subject2 = new Subject("TDT4242", courses);

        activity = new Activity(Arrays.asList(subject1), LocalDateTime.of(2023, 10, 29, 10, 0), LocalDateTime.of(2023, 10, 29, 12, 0), "Lecture on software development techniques");


    }

    @Test
    public void testActivityConstruction() {
        assertEquals("Activity for Programvareutvikling (TDT4140): 10:00 - 12:00. Description: Lecture on software development techniques", activity.toString());
    }

    @Test
    public void testAddSubject() {
        activity.addSubject(subject2);
        assertTrue(activity.toString().contains("Avansert programvareutvikling (TDT4242)"));
    }

    @Test
    public void testAddNullSubject() {
        assertThrows(IllegalArgumentException.class, () -> activity.addSubject(null));
    }

    @Test
    public void testRemoveSubject() {
        activity.removeSubject(subject1);
        assertFalse(activity.toString().contains("Programvareutvikling (TDT4140)"));
    }

    @Test
    public void testRemoveNullSubject() {
        assertThrows(IllegalArgumentException.class, () -> activity.removeSubject(null));
    }

    @Test
    public void testAddExistingSubject() {
        assertThrows(IllegalArgumentException.class, () -> activity.addSubject(subject1));
    }

    @Test
    public void testRemoveNonExistingSubject() {
        assertThrows(IllegalArgumentException.class, () -> activity.removeSubject(subject2));
    }

    @Test
    public void testValidateInputWithNullSubjects() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Test Description");
        });
    }

    @Test
    public void testValidateInputWithEmptySubjects() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(Collections.emptyList(), LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Test Description");
        });
    }

    @Test
    public void testValidateInputWithNullStartTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(List.of(new Subject("TDT4140", courses)), null, LocalDateTime.now().plusHours(1), "Test Description");
        });
        assertEquals("Start and end times cannot be null.", exception.getMessage());
    }
    
    @Test
    public void testValidateInputWithNullEndTime() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(List.of(new Subject("TDT4140", courses)), LocalDateTime.now(), null, "Test Description");
        });
    }

    @Test
    public void testValidateInputWithEndTimeBeforeStartTime() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(List.of(new Subject("TDT4140", courses)), LocalDateTime.now(), LocalDateTime.now().minusHours(1), "Test Description");
        });
    }

    @Test
    public void testValidateInputWithNullDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(List.of(new Subject("TDT4140", courses)), LocalDateTime.now(), LocalDateTime.now().plusHours(1), null);
        });
    }

    @Test
    public void testValidateInputWithEmptyDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(List.of(new Subject("TDT4140", courses)), LocalDateTime.now(), LocalDateTime.now().plusHours(1), "");
        });
    }

    @Test
    public void testValidateInputWithWhitespaceDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(List.of(new Subject("TDT4140", courses)), LocalDateTime.now(), LocalDateTime.now().plusHours(1), "   ");
        });
    }
    
}
