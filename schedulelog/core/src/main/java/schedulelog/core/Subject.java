package schedulelog.core;

/**
 * The Subject class represents an individual course with its code and name.
 */
public class Subject {

    // Variables to store the course code and name
    private String code;
    private String name;

    /**
     * Constructor to initialize the course code and retrieve its name from the
     * provided Courses.
     * 
     * @param code The course code.
     * @param name The name of the course.
     * @throws IllegalArgumentException if code or name is null or empty or if the
     *                                  code is not found in the courses..
     */
    public Subject(String code, Courses courses) {

        // add check if code and coursename is valid and in subjects
        // set this.name from found subject in courses
        if (code == null || code.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code or name cannot be null or empty");
        }
        if (courses == null) {
            throw new IllegalArgumentException("Courses collection cannot be null");
        }
        if (!courses.getCourseName(code).equals(code)) {
            throw new IllegalArgumentException("Invalid course code. It is not part of the courses.");
        }

        this.code = code;
        this.name = courses.getCourseName(code);
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
