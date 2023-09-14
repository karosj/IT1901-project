module fxutil {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    exports fxutil.doc;
    opens fxutil to javafx.graphics, javafx.fxml;
}
