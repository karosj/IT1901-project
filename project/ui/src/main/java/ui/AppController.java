package ui;

import core.Core;

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

    private Core core;

    public AppController() {
        this.core = new Core(false);
    }

    public Core getCore() {
        return this.core;
    }

    public void setCore(Core core) {
        this.core = core;
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
        label.setText(core.getTextFromFile());
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
        core.setTextFile(textViewString);
        clearTextInput();
    }

    @FXML
    void handleSubmit() {
        // TODO
        setTextViewString(getTextInputString());
    }

}
