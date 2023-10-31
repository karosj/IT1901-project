package schedulelog.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    public void testRemoveSubject() {
        activity.removeSubject(subject1);
        assertFalse(activity.toString().contains("Programvareutvikling (TDT4140)"));
    }

    @Test
    public void testAddExistingSubject() {
        assertThrows(IllegalArgumentException.class, () -> activity.addSubject(subject1));
    }

    @Test
    public void testRemoveNonExistingSubject() {
        assertThrows(IllegalArgumentException.class, () -> activity.removeSubject(subject2));
    }
}