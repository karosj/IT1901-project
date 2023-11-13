package schedulelog.json;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import schedulelog.core.Activity;

/**
 * Manages HTTP communication with a RESTful service for operations related to
 * Activity objects.
 *
 * RestConsumer provides functionalities to interact with a RESTful API,
 * particularly for
 * retrieving and adding Activity objects. It encapsulates the complexities of
 * making HTTP
 * requests and processing responses. The class supports operations such as
 * fetching a list
 * of activities from a remote server and posting new activity data.
 *
 * It utilizes Jackson for JSON serialization and deserialization, ensuring that
 * Activity
 * objects are correctly mapped to and from JSON format when communicating with
 * the server.
 * The class is configured to handle Java time objects properly when serializing
 * and
 * deserializing JSON data.
 */
public class RestConsumer {

    private final ObjectMapper mapper;

    /**
     * Initializes a new instance of RestConsumer with a configured ObjectMapper.
     *
     * The ObjectMapper is set up with the JavaTimeModule to handle Java time
     * objects
     * and configured to not write dates as timestamps.
     */
    public RestConsumer() {
        this.mapper = getConfiguredMapper();
    }

    /**
     * Retrieves a list of Activity objects from a RESTful API.
     *
     * This method sends a GET request to the specified URL and processes the
     * response.
     * It deserializes the JSON response into a List of Activity objects.
     *
     * @return List of Activity objects retrieved from the server, or null if an
     *         error occurs.
     */
    public List<Activity> getActivities() {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL("http://localhost:8080/activities");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                result.append(output);
            }
            br.close(); // Close the BufferedReader
            conn.disconnect();

            // Now result.toString() contains the full JSON response
            // System.out.println("Output from Server .... \n" + result.toString());

            // Deserialize JSON response to List of Activity objects
            List<Activity> activities = mapper.readValue(result.toString(), new TypeReference<List<Activity>>() {
            });
            return activities;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a new Activity object to the server via a RESTful API.
     *
     * This method sends a POST request to the server, including the Activity object
     * in JSON format in the request body.
     *
     * @param activity The Activity object to be added to the server.
     * @return The server's response as a String, or null if an error occurs.
     */
    public String addActivity(Activity activity) {
        try {
            System.out.println("Sending POST request to server...");
            URL url = new URL("http://localhost:8080/addActivity");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            ObjectMapper mapper = getConfiguredMapper();
            String jsonInput = mapper.writeValueAsString(activity);

            OutputStream os = conn.getOutputStream();
            os.write(jsonInput.getBytes());
            os.flush();
            os.close();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED
                    && conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            StringBuilder response = new StringBuilder();
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            br.close();
            conn.disconnect();

            System.out.println("Server response .... \n" + response.toString());
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Configures and returns an ObjectMapper for JSON serialization and
     * deserialization.
     *
     * The returned ObjectMapper is registered with JavaTimeModule and configured to
     * not write dates as timestamps, suitable for handling JSON data with Java time
     * objects.
     *
     * @return Configured ObjectMapper.
     */
    private ObjectMapper getConfiguredMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}