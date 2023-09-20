package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * The FileStorage class provides methods to read from and write to a text file.
 */
public class FileStorage {
    
    /**
     * Reads the content of the "content.txt" file and returns it as a string.
     * @return The content of the file, or "No file found." if the file does not exist.
     */
    public String getTextFromFile() {
        StringBuilder content = new StringBuilder();
        try {
            File file = new File("content.txt");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                content.append(reader.nextLine()).append("\n");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("No file found.");
            content = new StringBuilder("No file found.");
            e.printStackTrace();
        }
        return content.toString().trim();
    }

    /**
     * Writes the provided text to the "content.txt" file.
     * If the file does not exist, it will be created.
     * @param text The text to be written to the file.
     */
    public void setTextFile(String text) {
        try {
            File file = new File("content.txt");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            
            try (PrintWriter writer = new PrintWriter("content.txt")) {
                writer.print(text);
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}



           
