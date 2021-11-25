package todolist.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import todolist.core.AbstractTodoList;
import todolist.core.TodoList;
import todolist.core.TodoModel;
import todolist.core.TodoSettings;
import todolist.core.TodoSettings.TodoItemsSortOrder;
import todolist.core.TodoSettingsListener;
import todolist.ui.util.SceneTarget;

/**
 * Controller for TodoModel objects.
 * Supports adding new TodoList objects and
 * selecting one for viewing and editing.
 */
public class TodoModelController implements TodoSettingsListener {

  private TodoModelAccess todoModelAccess;

  @FXML
  String userTodoListPath;

  @FXML
  String sampleTodoListResource;

  @FXML
  ComboBox<String> todoListsView;

  @FXML
  TodoListController todoListViewController;

  /**
   * Sets the TodoModelAccess for this controller,
   * so data can come from different sources.
   *
   * @param todoModelAccess the new TodoModelAccess to use
   */
  public void setTodoModelAccess(TodoModelAccess todoModelAccess) {
    this.todoModelAccess = todoModelAccess;
    updateTodoItemsProvider();
    updateTodoListsView(null);
    todoModelAccess.getTodoSettings().addTodoSettingsListener(this);
  }
  
  @Override
  public void todoSettingsChanged(TodoSettings settings, Collection<String> changedProperties) {
    if (changedProperties.contains(TodoSettings.TODO_ITEM_SORT_ORDER_SETTING)) {
      updateTodoItemsProvider();
    }
  }

  private void updateTodoItemsProvider() {
    TodoItemsSortOrder sortOrder = this.todoModelAccess.getTodoSettings().getTodoItemsSortOrder();
    todoListViewController.setTodoItemsProvider(TodoModel.getSortedTodoItemsProvider(sortOrder));
  }

  @FXML
  void initialize() {
    // kobler data til list-controll
    initializeTodoListsView();
    todoListViewController.setOnTodoListChanged(todoList -> {
      todoModelAccess.notifyTodoListChanged(todoList);
      return null;
    });
  }

  private String addNewTodoListText = "<add new todo list>";

  private void initializeTodoListsView() {
    todoListsView.setEditable(true);
    todoListsView.valueProperty().addListener((prop, oldName, newName) -> {
      // System.out.println("valueProperty: -> "
      //    + todoListsView.getSelectionModel().getSelectedItem() + " @ " + todoListsView.getSelectionModel().getSelectedIndex() + " -> "
      //    + (oldName != null ? ("\"" + oldName + "\"") : null) + " -> " 
      //    + (newName != null ? ("\"" + newName + "\"") : null));
      if (newName != null && (! todoModelAccess.isValidTodoListName(newName))) {
        // allow user to edit name
      } else if (oldName != null && newName != null
          && (! todoListsView.getItems().contains(newName))) {
        // either new name of dummy item or existing item
        if (addNewTodoListText.equals(oldName)) {
          // add as new list
          todoModelAccess.addTodoList(new TodoList(newName));
          updateTodoListsView(newName);
        } else {
          // update name
          todoModelAccess.renameTodoList(oldName, newName);
          updateTodoListsView(newName);
        }
      } else if (todoListsView.getSelectionModel().getSelectedIndex() == 0) {
        // run later to avoid conflicts with event processing
        Platform.runLater(() -> {
          todoListsView.getEditor().selectAll();
        });
      } else if (todoListsView.getSelectionModel().getSelectedIndex() >= 0) {
        AbstractTodoList todoList = getSelectedTodoList();
        if (! (todoList instanceof TodoList)) {
          // retrieve actual list
          todoList = todoModelAccess.getTodoList(todoList.getName());
        }
        todoListViewController.setTodoList(todoList instanceof TodoList tl ? tl : null);
      }
    });
  }

  AbstractTodoList getSelectedTodoList() {
    return todoModelAccess.getTodoList(todoListsView.getSelectionModel().getSelectedItem());
  }

  protected void updateTodoListsView(String newSelection) {
    List<String> items = new ArrayList<>();
    // dummy element used for creating new ones, with null name
    items.add(addNewTodoListText);
    items.addAll(todoModelAccess.getTodoListNames());
    todoListsView.getItems().setAll(items);
    if (newSelection != null) {
      todoListsView.setValue(newSelection);
    } else {
      todoListsView.getSelectionModel().select(todoListsView.getItems().size() > 1 ? 1 : 0);
    }
  }

  private Scene settingsScene = null;

  private Scene getSettingsScene() throws RuntimeException {
    if (this.settingsScene == null) {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TodoSettings.fxml"));
      try {
        Object root = fxmlLoader.load();
        TodoSettingsController settingsController = fxmlLoader.getController();
        settingsController.setTodoSettings(todoModelAccess.getTodoSettings());
        settingsController.setBackButtonTarget(new SceneTarget(todoListsView.getScene()));
        if (root instanceof Parent) {
          this.settingsScene = new Scene((Parent) root);
        } else if (root instanceof Scene) {
          this.settingsScene = (Scene) root;
        }
      } catch (IOException ioe) {
        throw new RuntimeException(ioe);
      }
    }
    return this.settingsScene;
  }
  
  @FXML
  void handleSettingsAction() {
    try {
      ((Stage) todoListsView.getScene().getWindow()).setScene(getSettingsScene());
    } catch (Exception e) {
      System.err.println("Couldn't load settings scene");
      e.getCause().printStackTrace();
    }
  }
}
