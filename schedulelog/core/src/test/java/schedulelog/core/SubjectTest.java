package schedulelog.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubjectTest {

    // Subject instance for testing purposes.
    private Subject subject;

    // Setting up the test environment before each test.
    @BeforeEach
    public void setup() {
        List<String> codes = Arrays.asList("TDT4140", "TDT4242");
        List<String> names = Arrays.asList("Programvareutvikling", "Avansert programvareutvikling");
        subject = new Subject(codes, names);
    }

    // Testing the constructor with predefined subjects.
    @Test
    public void testDefaultConstructor() {
        Subject defaultSubject = new Subject();
        assertNotNull(defaultSubject.getSubjectName("TMA4240"));
        assertEquals("Statistikk", defaultSubject.getSubjectName("TMA4240"));
    }

    // Testing the constructor with custom subject codes and names.
    @Test
    public void testConstructorWithParameters() {
        assertEquals("Programvareutvikling", subject.getSubjectName("TDT4140"));
    }

    // Testing invalid constructor input.
    @Test
    public void testConstructorWithInvalidParameters() {
        List<String> codes = Arrays.asList("TDT4140");
        List<String> names = Arrays.asList("Programvareutvikling", "Avansert programvareutvikling");
        assertThrows(IllegalArgumentException.class, () -> new Subject(codes, names));
    }

    // Testing adding a new subject.
    @Test
    public void testAddSubject() {
        subject.addSubject("TDT4170", "Maskinlæring");
        assertEquals("Maskinæring", subject.getSubjectName("TDT4170"));
    }

    // Testing adding a subject with existing code.
    @Test
    public void testAddExistingSubject() {
        assertThrows(IllegalArgumentException.class, () -> subject.addSubject("TDT4140", "Some Course"));
    }

    // Testing adding a subject with null or empty input.
    @Test
    public void testAddSubjectWithNullOrEmptyValues() {
        assertThrows(IllegalArgumentException.class, () -> subject.addSubject(null, "Some Course"));
        assertThrows(IllegalArgumentException.class, () -> subject.addSubject("TDT4171", ""));
    }

    // Testing removing a subject.
    @Test
    public void testRemoveSubject() {
        subject.removeSubject("TDT4140");
        assertNull(subject.getSubjectName("TDT4140"));
    }

    // Testing removing a non-existing subject.
    @Test
    public void testRemoveNonExistingSubject() {
        assertThrows(IllegalArgumentException.class, () -> subject.removeSubject("TDT4180"));
    }
    
    // Testing the string representation of the Subject class.
    @Test
    public void testToString() {
        String representation = subject.toString();
        assertTrue(representation.contains("TDT4140"));
        assertTrue(representation.contains("Programvareutvikling"));
    }
}
