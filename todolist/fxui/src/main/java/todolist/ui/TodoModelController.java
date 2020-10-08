package todolist.ui;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import todolist.core.TodoList;
import todolist.core.TodoModel;
import todolist.json.TodoPersistence;

public class TodoModelController {

  private TodoModelAccess todoModelAccess;

  private TodoPersistence todoPersistence = new TodoPersistence();

  @FXML
  String userTodoListPath;

  @FXML
  String sampleTodoListResource;

  @FXML
  ComboBox<String> todoListsView;

  @FXML
  TodoListController todoListViewController;

  public void setTodoModelAccess(TodoModelAccess todoModelAccess) {
    this.todoModelAccess = todoModelAccess;
    updateTodoListsView(null);
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

  private void initializeTodoListsView() {
    todoListsView.setEditable(true);
    todoListsView.valueProperty().addListener((prop, oldName, newName) -> {
      // must identify the case where newTodoList represents an edited name
      if (oldName != null && newName != null && (! todoListsView.getItems().contains(newName))) {
        // either new name of dummy item or existing item
        if (oldName == todoListsView.getItems().get(0)) {
          // add as new list
          todoModelAccess.addTodoList(new TodoList(newName));
          updateTodoListsView(newName);
        } else {
          // update name
          todoModelAccess.renameTodoList(oldName, newName);
          updateTodoListsView(newName);
        }
      }
    });
    todoListsView.getSelectionModel().selectedIndexProperty().addListener((prop, oldIndex, newIndex)
        -> {
          if (newIndex.intValue() == 0) {
            todoListsView.setValue("");
          } else {
            TodoList todoList = getSelectedTodoList();
            todoListViewController.setTodoList(todoList);
          }
    });
  }

  TodoList getSelectedTodoList() {
    return todoModelAccess.getTodoList(todoListsView.getSelectionModel().getSelectedItem());
  }

  protected void updateTodoListsView(String newSelection) {
    List<String> items = new ArrayList<>();
    // dummy element used for creating new ones, with null name
    items.add("<add new todo list>");
    items.addAll(todoModelAccess.getTodoListNames());
    todoListsView.getItems().setAll(items);
    if (newSelection != null) {
      todoListsView.setValue(newSelection);
    } else {
      todoListsView.getSelectionModel().select(todoListsView.getItems().size() > 1 ? 1 : 0);
    }
  }

  /*
  void autoSaveTodoList() {
    if (userTodoListPath != null) {
      Path path = Paths.get(System.getProperty("user.home"), userTodoListPath);
      try (Writer writer = new FileWriter(path.toFile(), StandardCharsets.UTF_8)) {
        todoPersistence.writeTodoModel(todoModelAccess, writer);
      } catch (IOException e) {
        System.err.println("Fikk ikke skrevet til todolist.json på hjemmeområdet");
      }
    }
  }
  */
}
