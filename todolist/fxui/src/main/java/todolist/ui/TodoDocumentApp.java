package todolist.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Variant of the app with document metaphor and File menu.
 */
public class TodoDocumentApp extends Application {

  private TodoDocumentAppController controller;

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TodoDocumentApp.fxml"));
    Parent parent = fxmlLoader.load();
    this.controller = fxmlLoader.getController();
    stage.setScene(new Scene(parent));
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    this.controller.writeConfig();
    super.stop();
  }

  public static void main(String[] args) {
    launch(TodoDocumentApp.class, args);
  }
}