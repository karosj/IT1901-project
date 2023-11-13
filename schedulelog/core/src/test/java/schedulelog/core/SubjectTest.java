package schedulelog.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for Subject.
 */
public class SubjectTest {

    private Courses courses;

    @BeforeEach
    public void setUp() {
        List<String> codes = Arrays.asList("TDT4140", "TDT4242");
        List<String> names = Arrays.asList("Programvareutvikling", "Avansert programvareutvikling");
        courses = new Courses(codes, names);
    }

    // Testing subject construction with a valid course code.
    @Test
    public void testSubjectConstructionWithValidCode() {
        Subject subject = new Subject("TDT4140", courses);
        assertEquals("TDT4140", subject.getCode());
        assertEquals("Programvareutvikling", subject.getName());
    }

    // Testing subject construction with an invalid course code.
    @Test
    public void testSubjectConstructionWithInvalidCode() {
        assertThrows(IllegalArgumentException.class, () -> new Subject("TDT9999", courses));
    }

    // Testing subject constructor with a null code.
    @Test
    public void testConstructorWithNullCode() {
        assertThrows(IllegalArgumentException.class, () -> new Subject(null, courses));
    }

    // Testing subject constructor with an empty string as code.
    @Test
    public void testConstructorWithEmptyCode() {
        assertThrows(IllegalArgumentException.class, () -> new Subject("", courses));
    }

    // Testing subject constructor with null courses.
    @Test
    public void testConstructorWithNullCourses() {
        assertThrows(IllegalArgumentException.class, () -> new Subject("TDT4140", null));
    }

    // Testing the toString method of Subject.
    @Test
    public void testToString() {
        Subject subject = new Subject("TDT4140", courses);
        String representation = subject.toString();
        assertTrue(representation.contains("TDT4140"));
        assertTrue(representation.contains("Programvareutvikling"));
    }
}
