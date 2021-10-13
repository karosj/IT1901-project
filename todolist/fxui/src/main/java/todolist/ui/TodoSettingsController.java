package todolist.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import todolist.core.TodoSettings;
import todolist.core.TodoSettings.TodoItemsSortOrder;
import todolist.ui.util.SceneTarget;

/**
 * Controller for editing TodoSettings.
 */
public class TodoSettingsController {
  
  private TodoSettings todoSettings = new TodoSettings();
  
  public TodoSettings getTodoSettings() {
    return todoSettings;
  }
  
  public void setTodoSettings(TodoSettings todoSettings) {
    this.todoSettings = todoSettings;
    updateView();
  }
  
  @FXML
  ChoiceBox<String> todoItemsSortOrderSelector;

  @FXML
  Button backAction;

  @FXML
  void initialize() {
    todoItemsSortOrderSelector.setOnAction(actionEvent -> {
      int ordinal = todoItemsSortOrderSelector.getSelectionModel().getSelectedIndex();
      getTodoSettings().setTodoItemsSortOrder(TodoItemsSortOrder.values()[ordinal]);
    });
    updateView();
  }

  public void setBackButtonTarget(SceneTarget sceneTarget) {
    backAction.setOnAction(sceneTarget.getActionEventHandler());
  }

  private void updateView() {
    TodoItemsSortOrder sortOrder = getTodoSettings().getTodoItemsSortOrder();
    todoItemsSortOrderSelector.getSelectionModel().select(sortOrder.ordinal());
  }
}