package schedulelog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileStorage {

    /**
     * Reads the content of the "content.json" file and returns it as a string.
     * @return The content of the file, or "No file found." if the file does not exist.
     */
    public String getTextFromFile() {
        try {
            File file = new File("content.json");
            ObjectMapper mapper = new ObjectMapper();
            Content content = mapper.readValue(file, Content.class);
            return content.getContent();
        } catch (IOException e) {
            System.out.println("No file found.");
            e.printStackTrace();
            return "No file found.";
        }
    }

    /**
     * Writes the provided text to the "content.json" file.
     * If the file does not exist, it will be created.
     * @param text The text to be written to the file.
     */
    public void setTextFile(String text) {
        try {
            File file = new File("content.json");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            ObjectMapper mapper = new ObjectMapper();
            Content content = new Content(text);
            mapper.writeValue(file, content);
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Inner class to model the JSON object
    public static class Content {
        private String content;

        public Content() {
            // Default constructor for Jackson
        }

        public Content(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
