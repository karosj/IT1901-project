package schedulelog.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Course class represents a collection of courses with their codes and
 * names.
 */
public class Courses {

  // A map to store course codes as keys and course names as values.
  private final Map<String, String> courseMap;

  /**
   * Default constructor initializes the courseMap with predefined courses.
   */
  public Courses() {
    courseMap = new HashMap<>();
    List<String> courseCodes = Arrays.asList("TMA4240", "IT1901", "TDT4120", "TDT4160");
    List<String> courseNames = Arrays.asList("Statistikk", "Informatikk prosjektarbeid I",
        "Algoritmer og datastrukturer", "Datamaskiner og digitalteknikk");

    for (int i = 0; i < courseCodes.size(); i++) {
      // Using the addCourse method to ensure that validation logic is applied.
      addCourse(courseCodes.get(i), courseNames.get(i));
    }
  }

  /**
   * Constructor that initializes the courseMap with provided course codes and
   * names.
   * 
   * @param courseCode List of course codes.
   * @param courseName List of corresponding course names.
   * @throws IllegalArgumentException if the sizes of courseCodes and courseNames
   *                                  are not equal or if lists are null.
   */
  public Courses(List<String> courseCodes, List<String> courseNames) {
    if (courseCodes == null || courseNames == null) {
      throw new IllegalArgumentException("Course codes and names cannot be null");
    }

    if (courseCodes.size() != courseNames.size()) {
      throw new IllegalArgumentException("Size of courseCodes and courseNames must be equal");
    }

    courseMap = new HashMap<>();
    for (int i = 0; i < courseCodes.size(); i++) {
      // Using the addCourse method to ensure that validation logic is applied.
      addCourse(courseCodes.get(i), courseNames.get(i));
    }
  }

  /**
   * Retrieves the name of a course given its code.
   * 
   * @param code The course code.
   * @return The name of the course, or null if the code is not found.
   */
  public String getCourseName(String code) {
    return courseMap.get(code);
  }

  /**
   * Adds a new course to the collection.
   * 
   * @param code The course code.
   * @param name The name of the course.
   * @throws IllegalArgumentException if code or name is null or empty or if
   *                                  course with the same code already exists.
   */
  public void addCourse(String code, String name) {
    if (code == null || code.trim().isEmpty() || name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Course code or name cannot be null or empty");
    }

    if (courseMap.containsKey(code)) {
      throw new IllegalArgumentException("Course with code " + code + " already exists");
    }

    courseMap.put(code, name);
  }

  /**
   * Removes a course from the collection by its code.
   * 
   * @param code The course code to be removed.
   * @throws IllegalArgumentException if course with the code does not exist.
   */
  public void removeCourse(String code) {
    if (!courseMap.containsKey(code)) {
      throw new IllegalArgumentException("Courses with code " + code + " does not exist");
    }

    courseMap.remove(code);
  }

  /**
   * Returns a string representation of the courses in the collection.
   * 
   * @return A string representation of the courses.
   */
  @Override
  public String toString() {
    return "Courses: " + courseMap;
  }

  public Map<String, String> getCourses() {
    return courseMap;
  }
}
