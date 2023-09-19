package ui;

import core.Calc;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class AppController {

    private Calc calc;

    public AppController() {
        calc = new Calc(0.0, 0.0, 0.0);
    }

    public Calc getCalc() {
        return calc;
    }

    public void setCalc(Calc calc) {
        this.calc = calc;
        //updateOperandsView();
    }

    @FXML
    private Label label;

    @FXML
    private TextField textInput;

    @FXML
    void initialize() {
        setInitialLabelContent();
    }

    private void setInitialLabelContent() {
        label.setText(getTextFromFile());
    }

    private String getTextFromFile() {
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

    private void setTextFile(String text) {
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

    private void clearTextInput() {
        textInput.setText("");
    }

    private String getLabelString() {
        return label.getText();
    }

    private String getTextInputString() {
        return textInput.getText();
    }

    private boolean hasText() {
        return ! getLabelString().isBlank();
    }

    private void setTextViewString(String textViewString) {
        label.setText(textViewString);
        setTextFile(textViewString);
        clearTextInput();
    }

    @FXML
    void handleSubmit() {
        // TODO
        setTextViewString(getTextInputString());
    }

}
