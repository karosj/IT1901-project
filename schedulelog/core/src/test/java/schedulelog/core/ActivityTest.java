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
        assertEquals("Activity for Programvareutvikling (TDT4140): 10:00 - 12:00. Description: Lecture on software development techniques", activity.toString(),"Activity toString does not match expected format or content.");
    }

    @Test
    public void testAddSubject() {
        activity.addSubject(subject2);
        assertTrue(activity.toString().contains("Avansert programvareutvikling (TDT4242)"),"Activity's string representation should include the added subject 'Avansert programvareutvikling (TDT4242)', but it does not.");
    }

    @Test
    public void testAddNullSubject() {
        assertThrows(IllegalArgumentException.class, () -> activity.addSubject(null),"Adding a null subject should throw an IllegalArgumentException");
    }

    @Test
    public void testRemoveSubject() {
        activity.removeSubject(subject1);
        assertFalse(activity.toString().contains("Programvareutvikling (TDT4140)"),"Activity's string representation should not include the removed subject 'Programvareutvikling (TDT4140)', but it does.");
    }

    @Test
    public void testRemoveNullSubject() {
        assertThrows(IllegalArgumentException.class, () -> activity.removeSubject(null),"Attempting to remove a null subject should throw an IllegalArgumentException");
    }

    @Test
    public void testAddExistingSubject() {
        assertThrows(IllegalArgumentException.class, () -> activity.addSubject(subject1),"Attempting to add an existing subject should throw an IllegalArgumentException");
    }

    @Test
    public void testRemoveNonExistingSubject() {
        assertThrows(IllegalArgumentException.class, () -> activity.removeSubject(subject2),"Attempting to remove a non-existing subject should throw an IllegalArgumentException.");
    }

    @Test
    public void testValidateInputWithNullSubjects() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Test Description");
        },"Validating input with null subjects list should throw an IllegalArgumentException.");
    }

    @Test
    public void testValidateInputWithEmptySubjects() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(Collections.emptyList(), LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Test Description");
        },"Validating input with an empty subjects list should throw an IllegalArgumentException.");
    }

    @Test
    public void testValidateInputWithNullStartTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(List.of(new Subject("TDT4140", courses)), null, LocalDateTime.now().plusHours(1), "Test Description");
        });
        assertEquals("Start and end times cannot be null.", exception.getMessage(),"The exception message should indicate that start and end times cannot be null.");
    }
    
    @Test
    public void testValidateInputWithNullEndTime() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(List.of(new Subject("TDT4140", courses)), LocalDateTime.now(), null, "Test Description");
        },"Validating input with a null end time should throw an IllegalArgumentException.");
    }

    @Test
    public void testValidateInputWithEndTimeBeforeStartTime() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(List.of(new Subject("TDT4140", courses)), LocalDateTime.now(), LocalDateTime.now().minusHours(1), "Test Description");
        },"Validating input where the end time is before the start time should throw an IllegalArgumentException.");
    }

    @Test
    public void testValidateInputWithNullDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(List.of(new Subject("TDT4140", courses)), LocalDateTime.now(), LocalDateTime.now().plusHours(1), null);
        },"Validating input with a null description should throw an IllegalArgumentException.");
    }

    @Test
    public void testValidateInputWithEmptyDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(List.of(new Subject("TDT4140", courses)), LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Validating input with an empty description should throw an IllegalArgumentException.");
        });
    }

    @Test
    public void testValidateInputWithWhitespaceDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            activity.validateInput(List.of(new Subject("TDT4140", courses)), LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Validating input with a whitespace-only description should throw an IllegalArgumentException.");
        });
    }
    
}
