package schedulelog.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * The Activity class represents a specific period of time devoted to a particular subject or set of subjects.
 * Activities have a start time, end time, a description, and are associated with specific subjects.
 */
public class Activity {

    // List of subject codes associated with the activity.
    private List<String> subjectCodes; 
    // Start time of the activity.
    private LocalDateTime startTime;
    // End time of the activity.
    private LocalDateTime endTime;
    // A textual description of the activity.
    private String description;
    // Reference to the Subject class to fetch names for the subject codes.
    private Subject subjectReference;

    /**
     * Constructor to initialize an activity.
     *
     * @param subjectCodes List of subject codes related to the activity.
     * @param startTime Start time of the activity.
     * @param endTime End time of the activity.
     * @param description A textual description of the activity.
     * @param subjectReference Reference to a Subject object to retrieve subject names.
     */
    public Activity(List<String> subjectCodes, LocalDateTime startTime, LocalDateTime endTime, String description, Subject subjectReference) {
        // Validating the input parameters.
        validateInput(subjectCodes, startTime, endTime, description, subjectReference);

        this.subjectCodes = new ArrayList<>(subjectCodes);
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.subjectReference = subjectReference;
    }

    /**
     * Validates the input parameters for the constructor.
     *
     * @param subjectCodes List of subject codes related to the activity.
     * @param startTime Start time of the activity.
     * @param endTime End time of the activity.
     * @param description A textual description of the activity.
     * @param subjectReference Reference to a Subject object to retrieve subject names.
     */
    private void validateInput(List<String> subjectCodes, LocalDateTime startTime, LocalDateTime endTime, String description, Subject subjectReference) {
        // Validation for null or empty subjectCodes list.
        if (subjectCodes == null || subjectCodes.isEmpty()) {
            throw new IllegalArgumentException("Subject codes cannot be null or empty.");
        }
        // Validation to ensure each subjectCode is valid.
        for (String code : subjectCodes) {
            if (subjectReference.getSubjectName(code) == null) {
                throw new IllegalArgumentException("Invalid subject code: " + code);
            }
        }
        // Validation for null start and end times.
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start and end times cannot be null.");
        }
        // Validation to ensure end time is after start time.
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time should be after start time.");
        }
        // Validation for null or empty description.
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        // Validation for null subjectReference.
        if (subjectReference == null) {
            throw new IllegalArgumentException("Subject reference cannot be null.");
        }
    }

    // Standard getter for subjectCodes. 
    // Returns a defensive copy of the list to ensure encapsulation.
    public List<String> getSubjectCodes() {
        return new ArrayList<>(subjectCodes);
    }

    // Setter for subjectCodes with validation.
    public void setSubjectCodes(List<String> subjectCodes) {
        if (subjectCodes == null || subjectCodes.isEmpty()) {
            throw new IllegalArgumentException("Subject codes cannot be null or empty.");
        }
        this.subjectCodes = new ArrayList<>(subjectCodes);
    }

    // Standard getter for startTime.
    public LocalDateTime getStartTime() {
        return startTime;
    }

    // Setter for startTime with validation.
    public void setStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null.");
        }
        if (endTime != null && !endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time should be after start time.");
        }
        this.startTime = startTime;
    }

    // Standard getter for endTime.
    public LocalDateTime getEndTime() {
        return endTime;
    }

    // Setter for endTime with validation.
    public void setEndTime(LocalDateTime endTime) {
        if (endTime == null) {
            throw new IllegalArgumentException("End time cannot be null.");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time should be after start time.");
        }
        this.endTime = endTime;
    }

    // Standard getter for description.
    public String getDescription() {
        return description;
    }

    // Setter for description with validation.
    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        this.description = description;
    }

    /**
     * Returns a list of subject names associated with this activity using the subjectReference.
     * 
     * @return List of associated subject names.
     */
    public List<String> getSubjectNames() {
        List<String> subjectNames = new ArrayList<>();
        for (String code : subjectCodes) {
            subjectNames.add(subjectReference.getSubjectName(code));
        }
        return subjectNames;
    }

    /**
     * Calculates the duration of the activity in minutes.
     * 
     * @return The duration of the activity in minutes.
     */
    public int calculateDuration() {
        return (int) ChronoUnit.MINUTES.between(startTime, endTime);
    }

    /**
     * Provides a string representation of the activity.
     *
     * @return String representation of the activity.
     */
    @Override
    public String toString() {
        return "Activity [subjectCodes:" + subjectCodes + ", subjectNames:" + getSubjectNames() + ", startTime:" + startTime + ", endTime:" + endTime + ", description:"
                + description + "]";
    }
}
