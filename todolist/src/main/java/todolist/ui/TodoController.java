package todolist.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import todolist.core.TodoItem;
import todolist.core.TodoList;
import todolist.json.TodoModule;

public class TodoController {

  private static final String todoListWithTwoItems =
      "{\"items\":[{\"text\":\"Øl\",\"checked\":false},{\"text\":\"Pizza\",\"checked\":true}]}";

  private TodoList todoList;
  private ObjectMapper mapper = new ObjectMapper();

  /**
   * Initializes the TodoController by filling a TodoList with default contents.
   */
  public TodoController() {
    // setter opp data
    mapper.registerModule(new TodoModule());
    Reader reader = null;
    try {
      // try to read file from home folder first
      try {
        reader = new FileReader(Paths.get(System.getProperty("user.home"), "todolist.json").toFile(),
            StandardCharsets.UTF_8);
      } catch (IOException ioex) {
        System.err.println("Fant ingen todolist.json på hjemmeområdet, prøver eksempelfil i stedet");
        // try sample-todolist.json from resources source folder instead
        URL url = getClass().getResource("sample-todolist.json");
        if (url != null) {
          reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        } else {
          System.err.println("Fant ikke eksempelfil, bruker innebygget json-string");
          // use embedded String
          reader = new StringReader(todoListWithTwoItems);
        }
      }
      todoList = mapper.readValue(reader, TodoList.class);
    } catch (IOException e) {
      todoList = new TodoList();
      TodoItem item1 = todoList.createTodoItem();
      item1.setText("Øl");
      TodoItem item2 = todoList.createTodoItem();
      item1.setText("Pizza");
      todoList.addTodoItem(item1);
      todoList.addTodoItem(item2);
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        // ignore
      }
    }
  }

  @FXML
  TextField newTodoItemText;

  @FXML
  ListView<TodoItem> todoListView;

  @FXML
  Button deleteTodoItemButton;

  @FXML
  Button checkTodoItemButton;

  private Collection<Button> selectionButtons;

  @FXML
  void initialize() {
    selectionButtons = List.of(deleteTodoItemButton, checkTodoItemButton);
    // kobler data til list-controll
    updateTodoListView();
    updateTodoListButtons();
    todoList.addTodoListListener(todoList -> updateTodoListView());
    todoListView.setCellFactory(listView -> new TodoItemListCell());
    todoListView.getSelectionModel().selectedItemProperty()
        .addListener((prop, oldValue, newValue) -> updateTodoListButtons());
  }

  protected void updateTodoListView() {
    List<TodoItem> viewList = todoListView.getItems();
    viewList.clear();
    viewList.addAll(todoList.getUncheckedTodoItems());
    viewList.addAll(todoList.getCheckedTodoItems());
  }

  private void updateTodoListButtons() {
    boolean disable = todoListView.getSelectionModel().getSelectedItem() == null;
    for (Button button : selectionButtons) {
      button.setDisable(disable);
    }
  }

  @FXML
  void handleNewTodoItemAction() {
    TodoItem item = todoList.createTodoItem();
    item.setText(newTodoItemText.getText());
    todoList.addTodoItem(item);
    saveTodoList();
  }

  @FXML
  void handleDeleteItemAction() {
    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
    if (item != null) {
      todoList.removeTodoItem(item);
      saveTodoList();
    }
  }

  @FXML
  void handleCheckItemAction() {
    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
    if (item != null) {
      item.setChecked(true);
      saveTodoList();
    }
  }

  public void saveTodoList() {
    try {
      Writer writer =
          new FileWriter(Paths.get(System.getProperty("user.home"), "todolist.json").toFile(), StandardCharsets.UTF_8);
      mapper.writerWithDefaultPrettyPrinter().writeValue(writer, todoList);
    } catch (IOException e) {
      System.err.println("Fikk ikke skrevet til todolist.json på hjemmeområdet");
    }
  }
}
