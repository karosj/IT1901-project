package todolist.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import todolist.core.TodoList;

public class TodoModelController {

  private TodoModelAccess todoModelAccess;

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
        if (Objects.equals(oldName, todoListsView.getItems().get(0))) {
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
}
