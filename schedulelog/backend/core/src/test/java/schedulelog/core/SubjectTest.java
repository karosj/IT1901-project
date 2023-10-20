package schedulelog.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubjectTest {

    // Testing the construction of a Subject instance.
    @Test
    public void testSubjectConstruction() {
        Subject subject = new Subject("TDT4140", "Programvareutvikling");
        assertEquals("TDT4140", subject.getCode());
        assertEquals("Programvareutvikling", subject.getName());
    }

    // Testing the constructor with null or empty values.
    @Test
    public void testConstructorWithNullOrEmptyValues() {
        assertThrows(IllegalArgumentException.class, () -> new Subject(null, "Programvareutvikling"));
        assertThrows(IllegalArgumentException.class, () -> new Subject("", "Programvareutvikling"));
        assertThrows(IllegalArgumentException.class, () -> new Subject("TDT4140", null));
        assertThrows(IllegalArgumentException.class, () -> new Subject("TDT4140", ""));
    }

    // Testing the string representation of the Subject instance.
    @Test
    public void testToString() {
        Subject subject = new Subject("TDT4140", "Programvareutvikling");
        String representation = subject.toString();
        assertTrue(representation.contains("TDT4140"));
        assertTrue(representation.contains("Programvareutvikling"));
    }
}
