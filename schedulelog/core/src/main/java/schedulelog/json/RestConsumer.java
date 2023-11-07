package schedulelog.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import schedulelog.core.Activity;
import schedulelog.core.Courses;

import java.util.ArrayList;
import java.util.List;
import java.io.OutputStream;

public class RestConsumer {

    private final ObjectMapper mapper;

    public RestConsumer() {
        this.mapper = getConfiguredMapper();
    }

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
            System.out.println("Output from Server .... \n" + result.toString());

            // Deserialize JSON response to List of Activity objects
            List<Activity> activities = mapper.readValue(result.toString(), new TypeReference<List<Activity>>() {
            });
            return activities;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addActivity(Activity activity) {
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
        } catch (Exception e) {
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