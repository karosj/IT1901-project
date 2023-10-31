package schedulelog.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import schedulelog.core.Activity;
import java.util.ArrayList;
import java.util.List;


public class FileStorage {

    private static final String FILE_NAME = "activities.json";

    /**
     * Retrieves the list of activities from the "activities.json" file.
     * @return List of activities, or an empty list if the file does not exist.
     */
    public List<Activity> getActivities() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            ObjectMapper mapper = getConfiguredMapper();
            return mapper.readValue(file, new TypeReference<List<Activity>>() {});
        } catch (IOException e) {
            System.out.println("Error reading file.");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves the list of activities from the "activities.json" file as a JSON string.
     * @return JSON string representation of the list of activities.
     */
    public String getActivitiesJSON() {
        ObjectMapper mapper = getConfiguredMapper();
        try {
            return mapper.writeValueAsString(getActivities());
        } catch (IOException e) {
            System.out.println("Error converting activities to JSON.");
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Adds a new activity to the "activities.json" file.
     * @param activity The activity to be added.
     */
    public void addActivity(Activity activity) {
        try {
            File file = new File(FILE_NAME);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            System.out.println("to add:");
            System.out.println(activity);

            ObjectMapper mapper = getConfiguredMapper();

            List<Activity> activities = getActivities();
            activities.add(activity);

            System.out.println(activities);
            System.out.println("activities");
            
            mapper.writeValue(file, activities);
            System.out.println("Successfully added activity to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private ObjectMapper getConfiguredMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}