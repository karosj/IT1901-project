module todolist.ui {
    requires com.fasterxml.jackson.databind;

    requires java.net.http;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    requires todolist.core;
    requires fxutil;

    opens todolist.ui to javafx.graphics, javafx.fxml;
}
