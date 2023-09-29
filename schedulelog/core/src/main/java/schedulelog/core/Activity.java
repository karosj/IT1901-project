package schedulelog;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * The Activity class represents an activity associated with one or more subjects.
 * It contains details about the activity's start time, end time, and a description.
 */
public class Activity {
    // List of subject codes associated with the activity.
    private List<String> subjectCodes; 
    // Start time of the activity.
    private LocalDateTime startTime;
    // End time of the activity.
    private LocalDateTime endTime;
    // Description of the activity.
    private String description;
    // Reference to the Subject class to fetch subject names.
    private Subject subjectReference;

    /**
     * Constructor to initialize an activity with its associated subject codes, start time, end time, and description.
     * @param subjectCodes List of subject codes associated with the activity.
     * @param startTime Start time of the activity.
     * @param endTime End time of the activity.
     * @param description Description of the activity.
     * @param subjectReference Reference to the Subject class.
     */
    public Activity(List<String> subjectCodes, LocalDateTime startTime, LocalDateTime endTime, String description, Subject subjectReference) {
        this.subjectCodes = new ArrayList<>(subjectCodes);
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.subjectReference = subjectReference;
    }

    // Getter and setter methods for the class attributes.

    public List<String> getSubjectCodes() {
        return new ArrayList<>(subjectCodes);
    }

    public void setSubjectCodes(List<String> subjectCodes) {
        this.subjectCodes = new ArrayList<>(subjectCodes);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Fetches the names of the subjects based on the provided codes.
     * @return List of subject names.
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
     * @return Duration of the activity in minutes.
     * ChronoUnit comes from the java.time.temporal.ChronoUnit enumeration in Java's date-time API.
     * Represents units of time, like years, months, days, hours, minutes, etc.
     * 
     */
    public int calculateDuration() {
        return (int) ChronoUnit.MINUTES.between(startTime, endTime);
    }

    /**
     * Returns a string representation of the activity.
     * @return A string representation of the activity.
     */
    @Override
    public String toString() {
        return "Activity [subjectCodes=" + subjectCodes + ", subjectNames=" + getSubjectNames() + ", startTime=" + startTime + ", endTime=" + endTime + ", description="
                + description + "]";
    }
}
