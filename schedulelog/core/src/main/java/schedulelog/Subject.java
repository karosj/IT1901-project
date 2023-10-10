package schedulelog.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Subject class represents a collection of subjects with their codes and names.
 */
public class Subject {

    // A map to store subject codes as keys and subject names as values.
    private final Map<String, String> subjectMap;

    /**
     * Default constructor initializes the subjectMap with predefined subjects.
     */
    public Subject() {
        subjectMap = new HashMap<>();
        List<String> subjectCodes = Arrays.asList("TMA4240", "IT1901", "TDT4120", "TDT4160");
        List<String> subjectNames = Arrays.asList("Statistikk", "Informatikk prosjektarbeid I", "Algoritmer og datastrukturer", "Datamaskiner og digitalteknikk");
        
        for (int i = 0; i < subjectCodes.size(); i++) {
            // Using the addSubject method to ensure that validation logic is applied.
            addSubject(subjectCodes.get(i), subjectNames.get(i));
        }
    }

    /**
     * Constructor that initializes the subjectMap with provided subject codes and names.
     * @param subjectCodes List of subject codes.
     * @param subjectNames List of corresponding subject names.
     * @throws IllegalArgumentException if the sizes of subjectCodes and subjectNames are not equal or if lists are null.
     */
    public Subject(List<String> subjectCodes, List<String> subjectNames) {
        if(subjectCodes == null || subjectNames == null) {
            throw new IllegalArgumentException("Subject codes and names cannot be null");
        }

        if(subjectCodes.size() != subjectNames.size()) {
            throw new IllegalArgumentException("Size of subjectCodes and subjectNames must be equal");
        }

        subjectMap = new HashMap<>();
        for (int i = 0; i < subjectCodes.size(); i++) {
            // Using the addSubject method to ensure that validation logic is applied.
            addSubject(subjectCodes.get(i), subjectNames.get(i));
        }
    }

    /**
     * Retrieves the name of a subject given its code.
     * @param code The subject code.
     * @return The name of the subject, or null if the code is not found.
     */
    public String getSubjectName(String code) {
        return subjectMap.get(code);
    }

    /**
     * Adds a new subject to the collection.
     * @param code The subject code.
     * @param name The name of the subject.
     * @throws IllegalArgumentException if code or name is null or empty or if subject with the same code already exists.
     */
    public void addSubject(String code, String name) {
        if (code == null || code.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject code or name cannot be null or empty");
        }

        if (subjectMap.containsKey(code)) {
            throw new IllegalArgumentException("Subject with code " + code + " already exists");
        }
        
        subjectMap.put(code, name);
    }

    /**
     * Removes a subject from the collection by its code.
     * @param code The subject code to be removed.
     * @throws IllegalArgumentException if subject with the code does not exist.
     */
    public void removeSubject(String code) {
        if (!subjectMap.containsKey(code)) {
            throw new IllegalArgumentException("Subject with code " + code + " does not exist");
        }

        subjectMap.remove(code);
    }

    /**
     * Returns a string representation of the subjects in the collection.
     * @return A string representation of the subjects.
     */
    @Override
    public String toString() {
        return "Subjects: " + subjectMap;
    }
}


