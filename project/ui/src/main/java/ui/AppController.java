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
    private Label textView;

    @FXML
    private TextField textInput;

    @FXML
    void initialize() {
        setTextViewString(getTextFromFile());
    }

    private String getTextFromFile() {
        // TODO
        return "Hello world!";
    }

    private void setTextFile(String text) {
        // TODO
    }

    private String getTextViewString() {
        return textView.getText();
    }

    private String getTextInputString() {
        return textInput.getText();
    }

    private boolean hasText() {
        return ! getTextViewString().isBlank();
    }

    private void setTextViewString(String textViewString) {
        textView.setText(textViewString);
        setTextFile(textViewString);
    }

    @FXML
    void handleSubmit() {
        // TODO
        setTextViewString(getTextInputString());
    }

}
