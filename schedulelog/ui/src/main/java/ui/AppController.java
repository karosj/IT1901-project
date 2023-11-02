package ui;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import schedulelog.json.FileStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class AppController {

    private FileStorage fileStorage;

    public AppController() {
        fileStorage = new FileStorage();
    }

    public FileStorage getFileStorage() {
        return fileStorage;
    }

    public void setFileStorage(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }
/* 
    @FXML
    private Label label;

    @FXML
    private TextField textInput;

    @FXML
    void initialize() {
        setInitialLabelContent();
    }

    private void setInitialLabelContent() {
        label.setText("hey");
    }

    private void clearTextInput() {
        textInput.setText("");
    }

    private String getTextInputString() {
        return textInput.getText();
    }


    private void setLabelString(String labelString) {
        label.setText(labelString);
        //fileStorage.setTextFile(labelString);
        clearTextInput();
    }

    @FXML
    void handleSubmit() {
        // TODO
        setLabelString(getTextInputString());
    }*/

}
