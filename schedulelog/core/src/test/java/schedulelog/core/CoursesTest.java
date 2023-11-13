package schedulelog.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        assertNotNull(defaultCourse.getCourseName("TMA4240"));
        assertEquals("Statistikk", defaultCourse.getCourseName("TMA4240"));
    }

    // Testing the constructor with custom course codes and names.
    @Test
    public void testConstructorWithParameters() {
        assertEquals("Programvareutvikling", course.getCourseName("TDT4140"));
    }

    // Testing invalid constructor input.
    @Test
    public void testConstructorWithInvalidParameters() {
        List<String> codes = Arrays.asList("TDT4140");
        List<String> names = Arrays.asList("Programvareutvikling", "Avansert programvareutvikling");
        assertThrows(IllegalArgumentException.class, () -> new Courses(codes, names));
    }

    // Testing invalid constructor input.
    @Test
    public void testConstructorWithNullCourseCodes() {
        assertThrows(IllegalArgumentException.class, () -> new Courses(null, null));
    }

    // Testing adding a new course.
    @Test
    public void testAddCourse() {
        course.addCourse("TDT4170", "Maskinlæring");
        assertEquals("Maskinlæring", course.getCourseName("TDT4170"));
    }

    // Testing adding a course with existing code.
    @Test
    public void testAddExistingCourse() {
        assertThrows(IllegalArgumentException.class, () -> course.addCourse("TDT4140", "Some Course"));
    }

    // Testing adding a course with null or empty input.
    @Test
    public void testAddCourseWithNullOrEmptyValues() {
        assertThrows(IllegalArgumentException.class, () -> course.addCourse(null, "Some Course"));
        assertThrows(IllegalArgumentException.class, () -> course.addCourse("TDT4171", ""));
    }

    // Testing removing a course.
    @Test
    public void testRemoveCourse() {
        course.removeCourse("TDT4140");
        assertNull(course.getCourseName("TDT4140"));
    }

    // Testing removing a non-existing course.
    @Test
    public void testRemoveNonExistingCourse() {
        assertThrows(IllegalArgumentException.class, () -> course.removeCourse("TDT4180"));
    }

    // Testing getting courses
    @Test
    public void testGetCourses() {
        Map<String, String> expectedCourses = Map.of(
            "TDT4140", "Programvareutvikling",
            "TDT4242", "Avansert programvareutvikling"
        );
        assertTrue(course.getCourses().equals(expectedCourses), "Courses map should match the expected map");
    }
    
    // Testing the string representation of the Courses class.
    @Test
    public void testToString() {
        String representation = course.toString();
        assertTrue(representation.contains("TDT4140"));
        assertTrue(representation.contains("Programvareutvikling"));
    }
}
