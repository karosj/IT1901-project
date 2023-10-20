package schedulelog.core;

/**
 * The Subject class represents an individual course with its code and name.
 */
public class Subject {

    // Variables to store the course code and name
    private String code;
    private String name;

    /**
     * Constructor to initialize the course code and name.
     * 
     * @param code The course code.
     * @param name The name of the course.
     * @throws IllegalArgumentException if code or name is null or empty.
     */
    public Subject(String code, String name) {
        if (code == null || code.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code or name cannot be null or empty");
        }

        this.code = code;
        this.name = name;
    }

    /**
     * Retrieves the course code.
     * 
     * @return The course code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Retrieves the name of the course.
     * 
     * @return The name of the course.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the course.
     * 
     * @return A string representation of the course.
     */
    @Override
    public String toString() {
        return "Course Code: " + code + ", Course Name: " + name;
    }
}
