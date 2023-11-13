package ui;

import java.io.IOException;

import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * TestFX App test
 */
public class AppTest extends ApplicationTest {

    private AppController controller;
    private App app;
    private Parent root;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("App.fxml"));
        root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        stage.setScene(new Scene(root));
        stage.show();
        app = new App();
    }

    public Parent getRootNode() {
        return root;
    }

    /*
     * private void click(String... labels) {
     * for (var label : labels) {
     * clickOn(LabeledMatchers.hasText(label));
     * }
     * }
     * 
     * @Test
     * public void testControllerInitial() {
     * assertNotNull(this.controller);
     * }
     * 
     * @Test
     * public void testAppInitial() {
     * assertNotNull(this.app);
     * }
     * 
     * @Test
     * public void checkTextBoxAndLabel() {
     * clickOn("#textInput").write("Hello, World!");
     * click("Submit");
     * }
     */
}
