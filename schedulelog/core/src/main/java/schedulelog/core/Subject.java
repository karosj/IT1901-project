package schedulelog.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Subject class represents an individual course with its code and name.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Subject {

  // Variables to store the course code and course name
  private String code;
  private String name;
  private Courses courses;

  // Default constructor for Jackson
  public Subject() {
    this.courses = new Courses();
  }

  /**
   * Constructor to initialize the course code and retrieve its name from the
   * provided Courses.
   *
   * @param code    The course code.
   * @param courses The courses available.
   * @throws IllegalArgumentException if code is null or empty or if the
   *                                  code is not found in the courses.
   */
  public Subject(String code, Courses courses) {
    if (code == null || code.trim().isEmpty()) {
      throw new IllegalArgumentException("Course code cannot be null or empty");
    }
    if (courses == null) {
      throw new IllegalArgumentException("Courses name cannot be null");
    }
    this.name = courses.getCourseName(code);
    if (this.name == null) {
      throw new IllegalArgumentException("Invalid course code. It is not part of the courses.");
    }

    this.code = code;
    this.courses = courses;
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
   * Resets the name of the current instance based on the course code.
   * 
   * This method updates the 'name' field of the current object by fetching the
   * course name
   * associated with the 'code' field from a 'courses' data source.
   */

  public void resetName() {
    this.name = courses.getCourseName(code);
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