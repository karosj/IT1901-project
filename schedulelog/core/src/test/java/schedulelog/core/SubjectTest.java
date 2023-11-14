package schedulelog.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class for Subject
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
        assertEquals("TDT4140", subject.getCode(),"Subject code should be 'TDT4140' when constructed with a valid code.");
        assertEquals("Programvareutvikling", subject.getName(),"Subject name should be 'Programvareutvikling' when constructed with a valid code 'TDT4140'.");
    }

    // Testing subject construction with an invalid course code.
    @Test
    public void testSubjectConstructionWithInvalidCode() {
        assertThrows(IllegalArgumentException.class, () -> new Subject("TDT9999", courses),"Constructing a subject with an invalid code should throw an IllegalArgumentException.");
    }

    // Testing subject constructor with a null code.
    @Test
    public void testConstructorWithNullCode() {
        assertThrows(IllegalArgumentException.class, () -> new Subject(null, courses),"Constructing a subject with a null code should throw an IllegalArgumentException.");
    }

    // Testing subject constructor with an empty string as code.
    @Test
    public void testConstructorWithEmptyCode() {
        assertThrows(IllegalArgumentException.class, () -> new Subject("", courses),"Constructing a subject with an empty code should throw an IllegalArgumentException.");
    }

    // Testing subject constructor with null courses.
    @Test
    public void testConstructorWithNullCourses() {
        assertThrows(IllegalArgumentException.class, () -> new Subject("TDT4140", null),"Constructing a subject with null courses should throw an IllegalArgumentException.");
    }

    // Testing the toString method of Subject.
    @Test
    public void testToString() {
        Subject subject = new Subject("TDT4140", courses);
        String representation = subject.toString();
        assertTrue(representation.contains("TDT4140"),"Subject's string representation should contain the code 'TDT4140'.");
        assertTrue(representation.contains("Programvareutvikling"),"Subject's string representation should contain the name 'Programvareutvikling'.");
    }
}
