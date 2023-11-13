package schedulelog.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import schedulelog.core.Activity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import schedulelog.core.Activity;

/**
 * FileStorage functionality for managing the storage and retrieval of Activity
 * objects.  
 * Handles interactions with a JSON file (activities.json) for persisting
 * activity data. Offers methods to read activities from the file, convert them
 * to
 * JSON format, add new activities, and configure JSON processing for proper
 * handling
 * of date and time formats. This class is essential for maintaining the
 * persistent state
 * of activities across application restarts.
 */
public class FileStorage {

  private final String FILE_NAME;

    /**
     * Constructor to initialize a filename "activities.json"
     */
    public FileStorage() {
        this.FILE_NAME = "activities.json";
    }

    /**
     * Constructor to initialize an activity.
     * 
     * @param fileName name of the file.
     */
    public FileStorage(String fileName) {
        this.FILE_NAME = fileName;
    }
  }

  /**
   * Retrieves the list of activities from the "activities.json" file as a JSON
   * string.
   * 
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
   * 
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

      mapper.writeValue(file, activities);
      System.out.println("Successfully added activity to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  /*
   * Gets the file name.
   * 
   * @return The file name.
   */
  public String getFileName() {
    return FILE_NAME;
  }

            ObjectMapper mapper = getConfiguredMapper();

            List<Activity> activities = getActivities();
            activities.add(activity);

            mapper.writeValue(file, activities);
            System.out.println("Successfully added activity to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /*
     * Gets the file name.
     * 
     * @return The file name.
     */
    public String getFileName() {
        return FILE_NAME;
    }

    /**
     * Creates and configures an ObjectMapper for JSON processing.
     *
     * Initializes a new ObjectMapper and configures it for handling
     * Java time objects by registering the JavaTimeModule. It also adjusts the
     * ObjectMapper's settings to not write dates as timestamps.
     *
     * @return A configured ObjectMapper instance.
     */
    private ObjectMapper getConfiguredMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}