package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;


public class FileStorage {
    
    public String getTextFromFile() {
        String content = "";
        try {
            File file = new File("content.txt");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
              content += reader.nextLine();
            }
            reader.close();
          } catch (FileNotFoundException e) {
                System.out.println("No file found.");
                content = "No file found.";
            e.printStackTrace();
          }

        return content;
    }

    public void setTextFile(String text) {
        try {
            File file = new File("content.txt");
        if (file.createNewFile()) {
            System.out.println("File created: " + file.getName());

        } else {
            System.out.println("File already exists.");
        }
        try {
            PrintWriter writer = new PrintWriter("content.txt");
            writer.print(text);
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        e.printStackTrace();
        }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}