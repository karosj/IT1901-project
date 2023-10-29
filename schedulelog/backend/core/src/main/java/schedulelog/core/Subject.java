package schedulelog.core;

/**
 * The Subject class represents an individual course with its code and name.
 */
public class Subject {

    // Variables to store the course code and course name
    private final String code;
    private final Courses name;

    /**
     * Constructor to initialize the course code and retrieve its name from the
     * provided Courses.
     *
     * @param code The course code.
     * @param name The name of the course.
     * @throws IllegalArgumentException if code is null or empty or if the
     *                                  code is not found in the courses.
     */
    public Subject(String code, Courses name) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be null or empty");
        }
        if (name == null) {
            throw new IllegalArgumentException("Course name cannot be null");
        }
        String courseName = name.getCourseName(code);
        if (courseName == null) {
            throw new IllegalArgumentException("Invalid course code. It is not part of the courses.");
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
    public String getCourseName() {
        return name.getCourseName(code);
    }

    /**
     * Returns a string representation of the course.
     *
     * @return A string representation of the course.
     */
    @Override
    public String toString() {
        return "Course Code: " + code + ", Course Name: " + getCourseName();
    }
}
