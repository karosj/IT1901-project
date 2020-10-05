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

  private TodoModel todoModel;

  private TodoPersistence todoPersistence = new TodoPersistence();

  @FXML
  String userTodoListPath;

  @FXML
  String sampleTodoListResource;

  @FXML
  ComboBox<TodoList> todoListsView;

  @FXML
  TodoListController todoListViewController;

  public TodoModel getTodoModel() {
    return todoModel;
  }

  public void setTodoModel(TodoModel todoModel) {
    this.todoModel = todoModel;
    updateTodoListsView(null);
  }

  @FXML
  void initialize() {
    // kobler data til list-controll
    initializeTodoListsView();
    todoListViewController.setOnTodoListChangedCallback(todoList -> {
      autoSaveTodoList();
      return null;
    });
  }

  private void initializeTodoListsView() {
    todoListsView.setCellFactory(listView  -> {
      ListCell<TodoList> listCell = new ListCell<TodoList>() {
        @Override
        protected void updateItem(TodoList item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setText(null);
          } else if (item.getName() == null) {
            setText("<create new list>");
          } else {
            setText(item.getName());
          }
        }
      };
      return listCell;
    });
    todoListsView.setConverter(new StringConverter<TodoList>() {
      @Override
      public String toString(TodoList todoList) {
        return (todoList != null ? todoList.getName() : "???");
      }

      @Override
      public TodoList fromString(String newName) {
        TodoList todoList = new TodoList(); // getTodoList()
        todoList.setName(newName);
        return todoList;
      }
    });
    todoListsView.setEditable(true);
    todoListsView.valueProperty().addListener((prop, oldList, newList) -> {
      // must identify the case where newTodoList represents an edited name
      if (oldList != null && newList != null && (! todoListsView.getItems().contains(newList))) {
        // either new name of dummy item or existing item
        if (oldList.getName() == null) {
          // add as new list
          todoModel.addTodoList(newList);
          updateTodoListsView(newList);
        } else {
          // update name
          oldList.setName(newList.getName());
          updateTodoListsView(oldList);
        }
      }
    });
    todoListsView.getSelectionModel().selectedItemProperty().addListener((prop, oldList, newList)
        -> {
      todoListViewController.setTodoList(getSelectedTodoList());
    });
  }

  TodoList getSelectedTodoList() {
    return todoListsView.getSelectionModel().getSelectedItem();
  }

  protected void updateTodoListsView(TodoList newSelection) {
    List<TodoList> items = new ArrayList<>();
    // dummy element used for creating new ones, with null name
    items.add(new TodoList());
    todoModel.forEach(items::add);
    todoListsView.getItems().setAll(items);
    if (newSelection != null) {
      todoListsView.setValue(newSelection);
    } else {
      todoListsView.getSelectionModel().select(todoListsView.getItems().size() > 1 ? 1 : 0);
    }
  }

  void autoSaveTodoList() {
    if (userTodoListPath != null) {
      Path path = Paths.get(System.getProperty("user.home"), userTodoListPath);
      try (Writer writer = new FileWriter(path.toFile(), StandardCharsets.UTF_8)) {
        todoPersistence.writeTodoModel(todoModel, writer);
      } catch (IOException e) {
        System.err.println("Fikk ikke skrevet til todolist.json på hjemmeområdet");
      }
    }
  }
}
