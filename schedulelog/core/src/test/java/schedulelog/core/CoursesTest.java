package schedulelog.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class for Courses
 */
public class CoursesTest {

    // Courses instance for testing purposes.
    private Courses course;

    // Setting up the test environment before each test.
    @BeforeEach
    public void setup() {
        List<String> codes = Arrays.asList("TDT4140", "TDT4242");
        List<String> names = Arrays.asList("Programvareutvikling", "Avansert programvareutvikling");
        course = new Courses(codes, names);
    }

    // Testing the constructor with predefined courses.
    @Test
    public void testDefaultConstructor() {
        Courses defaultCourse = new Courses();
        assertNotNull(defaultCourse.getCourseName("TMA4240"),"Course name for 'TMA4240' should not be null in the default constructor.");
        assertEquals("Statistikk", defaultCourse.getCourseName("TMA4240"),"Course name for 'TMA4240' should be 'Statistikk' in the default constructor.");
    }

    // Testing the constructor with custom course codes and names.
    @Test
    public void testConstructorWithParameters() {
        assertEquals("Programvareutvikling", course.getCourseName("TDT4140"), "The course name for 'TDT4140' should be 'Programvareutvikling'");
    }

    // Testing invalid constructor input.
    @Test
    public void testConstructorWithInvalidParameters() {
        List<String> codes = Arrays.asList("TDT4140");
        List<String> names = Arrays.asList("Programvareutvikling", "Avansert programvareutvikling");
        assertThrows(IllegalArgumentException.class, () -> new Courses(codes, names),"Constructor should throw an IllegalArgumentException for mismatched lengths of course codes and names.");
    }

    // Testing invalid constructor input.
    @Test
    public void testConstructorWithNullCourseCodes() {
        assertThrows(IllegalArgumentException.class, () -> new Courses(null, null),"Constructor should throw an IllegalArgumentException when given null for both course codes and names.");
    }

    // Testing adding a new course.
    @Test
    public void testAddCourse() {
        course.addCourse("TDT4170", "Maskinlæring");
        assertEquals("Maskinlæring", course.getCourseName("TDT4170"),"Course name for 'TDT4170' should be 'Maskinlæring' after adding the course.");
    }

    // Testing adding a course with existing code.
    @Test
    public void testAddExistingCourse() {
        assertThrows(IllegalArgumentException.class, () -> course.addCourse("TDT4140", "Some Course"),"Adding a course with an existing code should throw an IllegalArgumentException.");
    }

    // Testing adding a course with null or empty input.
    @Test
    public void testAddCourseWithNullOrEmptyValues() {
        assertThrows(IllegalArgumentException.class, () -> course.addCourse(null, "Some Course"),"Adding a course with a null code should throw an IllegalArgumentException.");
        assertThrows(IllegalArgumentException.class, () -> course.addCourse("TDT4171", ""),"Adding a course with an empty name should throw an IllegalArgumentException.");
    }

    // Testing removing a course.
    @Test
    public void testRemoveCourse() {
        course.removeCourse("TDT4140");
        assertNull(course.getCourseName("TDT4140"),"Course name for 'TDT4140' should be null after removing the course.");
    }

    // Testing removing a non-existing course.
    @Test
    public void testRemoveNonExistingCourse() {
        assertThrows(IllegalArgumentException.class, () -> course.removeCourse("TDT4180"),"Removing a non-existing course should throw an IllegalArgumentException.");
    }

    // Testing getting courses
    @Test
    public void testGetCourses() {
        Map<String, String> expectedCourses = Map.of(
                "TDT4140", "Programvareutvikling",
                "TDT4242", "Avansert programvareutvikling");
        assertTrue(course.getCourses().equals(expectedCourses), "Courses map should match the expected map");
    }

    // Testing the string representation of the Courses class.
    @Test
    public void testToString() {
        String representation = course.toString();
        assertTrue(representation.contains("TDT4140"),"Courses string representation should contain the code 'TDT4140'.");
        assertTrue(representation.contains("Programvareutvikling"),"Courses string representation should contain the name 'Programvareutvikling'.");
    }
}
