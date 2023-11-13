package schedulelog.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The Activity class represents a specific period of time devoted to a
 * particular subject or set of subjects.
 * Activities have a start time, end time, a description, and are associated
 * with specific subjects.
 */
public class Activity {

    // List of subjects associated with the activity.
    private List<Subject> subjects = new ArrayList<>();

    // Start time of the activity.
    private LocalDateTime startTime;

    // End time of the activity.
    private LocalDateTime endTime;

    // A textual description of the activity.
    private String description;

    // Default constructor for Jackson
    public Activity() {

    }

    /**
     * Constructor to initialize an activity.
     *
     * @param subjects    List of subjects related to the activity.
     * @param startTime   Start time of the activity.
     * @param endTime     End time of the activity.
     * @param description A textual description of the activity.
     */
    public Activity(List<Subject> subjects, LocalDateTime startTime, LocalDateTime endTime, String description) {
        // Validating the input parameters.
        validateInput(subjects, startTime, endTime, description);

        this.subjects.addAll(subjects);
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    /**
     * Validates the input parameters for the constructor.
     *
     * @param subjects    List of subjects related to the activity.
     * @param startTime   Start time of the activity.
     * @param endTime     End time of the activity.
     * @param description A textual description of the activity.
     */
    public void validateInput(List<Subject> subjects, LocalDateTime startTime, LocalDateTime endTime,
            String description) {
        if (subjects == null || subjects.isEmpty()) {
            throw new IllegalArgumentException("List of subjects cannot be null or empty.");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start and end times cannot be null.");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time should be after start time.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
    }

    /**
     * Adds a subject to the activity.
     *
     * @param subject The subject to be added.
     */
    public void addSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null.");
        }
        if (subjects.contains(subject)) {
            throw new IllegalArgumentException("Subject already associated with this activity.");
        }
        subjects.add(subject);
    }

    /**
     * Removes a subject from the activity.
     *
     * @param subject The subject to be removed.
     */
    public void removeSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null.");
        }
        if (!subjects.remove(subject)) {
            throw new IllegalArgumentException("Subject not associated with this activity.");
        }
    }

    /**
     * Retrieves the list of subjects.
     * 
     * @return A list of Subject objects associated with this instance.
     */
    public List<Subject> getSubjects() {
        return subjects;
    }

    /**
     * Gets the start time.
     * 
     * @return The LocalDateTime representing the start time.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time.
     * 
     * @return The LocalDateTime representing the end time.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Gets the description.
     * 
     * @return A string representing the description of the instance.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Provides a string representation of the activity.
     *
     * @return String representation of the activity.
     */
    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();

        // Using DateTimeFormatter to format time.
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Loop through all subjects associated with the activity.
        for (Subject subject : subjects) {
            representation.append("Activity for ").append(subject.getName()).append(" (").append(subject.getCode())
                    .append("): ");
        }

        // Format the start and end times.
        representation.append(startTime.format(timeFormatter)).append(" - ").append(endTime.format(timeFormatter))
                .append(". Description: ").append(description);

        return representation.toString();
    }
}