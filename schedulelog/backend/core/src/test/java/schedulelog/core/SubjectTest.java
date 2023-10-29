package schedulelog.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubjectTest {

    private Courses courses;

    @BeforeEach
    public void setUp() {
        List<String> codes = Arrays.asList("TDT4140", "TDT4242");
        List<String> names = Arrays.asList("Programvareutvikling", "Avansert programvareutvikling");
        courses = new Courses(codes, names);
    }

    @Test
    public void testSubjectConstructionWithValidCode() {
        Subject subject = new Subject("TDT4140", courses);
        assertEquals("TDT4140", subject.getCode());
        assertEquals("Programvareutvikling", subject.getCourseName());
    }

    @Test
    public void testSubjectConstructionWithInvalidCode() {
        assertThrows(IllegalArgumentException.class, () -> new Subject("TDT9999", courses));
    }

    @Test
    public void testConstructorWithNullCode() {
        assertThrows(IllegalArgumentException.class, () -> new Subject(null, courses));
    }

    @Test
    public void testConstructorWithEmptyCode() {
        assertThrows(IllegalArgumentException.class, () -> new Subject("", courses));
    }

    @Test
    public void testConstructorWithNullCourses() {
        assertThrows(IllegalArgumentException.class, () -> new Subject("TDT4140", null));
    }

    @Test
    public void testToString() {
        Subject subject = new Subject("TDT4140", courses);
        String representation = subject.toString();
        assertTrue(representation.contains("TDT4140"));
        assertTrue(representation.contains("Programvareutvikling"));
    }
}
