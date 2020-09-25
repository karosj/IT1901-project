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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import todolist.core.TodoItem;
import todolist.core.TodoList;
import todolist.json.TodoModule;

public class TodoController {

  private static final String todoListWithTwoItems =
      "{\"items\":[{\"text\":\"Øl\",\"checked\":false},{\"text\":\"Pizza\",\"checked\":true}]}";

  private TodoList todoList;

  // makes class more testable
  TodoList getTodoList() {
    return todoList;
  }

  private ObjectMapper mapper = new ObjectMapper();

  @FXML
  String userTodoListPath;

  @FXML
  String sampleTodoListResource;

  @FXML
  TextField newTodoItemText;

  @FXML
  ListView<TodoItem> todoListView;

  @FXML
  Button deleteTodoItemButton;

  private void initializeTodoList() {
    // setter opp data
    mapper.registerModule(new TodoModule());
    Reader reader = null;
    // try to read file from home folder first
    if (userTodoListPath != null) {
      try {
        reader = new FileReader(Paths.get(System.getProperty("user.home"), userTodoListPath)
            .toFile(), StandardCharsets.UTF_8);
      } catch (IOException ioex) {
        System.err.println("Fant ingen " + userTodoListPath + " på hjemmeområdet");
      }
    }
    if (reader == null && sampleTodoListResource != null) {
      // try sample todo list from resources instead
      URL url = getClass().getResource(sampleTodoListResource);
      if (url != null) {
        try {
          reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
          System.err.println("Kunne ikke lese innebygget " + sampleTodoListResource);
        }
      } else {
        System.err.println("Fant ikke innebygget " + sampleTodoListResource);
      }
    }
    if (reader == null) {
      // use embedded String
      reader = new StringReader(todoListWithTwoItems);
    }
    try {
      todoList = mapper.readValue(reader, TodoList.class);
    } catch (IOException e) {
      todoList = new TodoList(
        todoList.createTodoItem().text("Øl"),
        todoList.createTodoItem().text("Pizza")
      );
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

  private Collection<Button> selectionButtons;

  @FXML
  void initialize() {
    initializeTodoList();
    selectionButtons = List.of(deleteTodoItemButton);
    // kobler data til list-controll
    updateTodoListView();
    updateTodoListButtons();
    todoList.addTodoListListener(todoList -> {
      autoSaveTodoList();
      updateTodoListView();
    });
    TodoItemListCellDragHandler dragHandler = new TodoItemListCellDragHandler(todoList);
    todoListView.setCellFactory(listView -> {
      TodoItemListCell listCell = new TodoItemListCell();
      dragHandler.registerHandlers(listCell);
      return listCell;
    });
    todoListView.getSelectionModel().selectedItemProperty()
        .addListener((prop, oldValue, newValue) -> updateTodoListButtons());
    todoListView.setEditable(true);
  }

  protected void updateTodoListView() {
    List<TodoItem> items = new ArrayList<>();
    items.addAll(todoList.getUncheckedTodoItems());
    items.addAll(todoList.getCheckedTodoItems());
    todoListView.getItems().setAll(items);
  }

  private void updateTodoListButtons() {
    boolean disable = todoListView.getSelectionModel().getSelectedItem() == null;
    for (Button button : selectionButtons) {
      button.setDisable(disable);
    }
    double rowLayoutY = getRowLayoutY(todoListView, listCell -> isSelected(todoListView, listCell), 0);
    System.out.println(rowLayoutY);
  }

  private boolean isSelected(ListView<?> listView, ListCell<?> listCell) {
    return isSelected(listView, listCell.getItem());
  }
  private boolean isSelected(ListView<?> listView, Object item) {
    return todoListView.getSelectionModel().getSelectedItems().contains(item);
  }

  @SuppressWarnings("unchecked")
  private <T> double getRowLayoutY(ListView<T> listView, Predicate<ListCell<T>> test, int num) {
    for (Node child : listView.lookupAll(".list-cell")) {
      if (child instanceof ListCell) {
        ListCell<T> listCell = (ListCell<T>) child;
        if (test.test(listCell) && num-- == 0) {
          double dy = 0;
          Node node = listCell;
          while (node != todoListView) {
            dy += node.getLayoutY();
            node = node.getParent();
          }
          return dy;
        }
      }
    }
    return -1;
  }

  @FXML
  void handleNewTodoItemAction() {
    TodoItem item = todoList.createTodoItem();
    item.setText(newTodoItemText.getText());
    todoList.addTodoItem(item);
  }

  @FXML
  void handleDeleteItemAction() {
    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
    if (item != null) {
      todoList.removeTodoItem(item);
    }
  }

  @FXML
  void handleCheckItemAction() {
    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
    if (item != null) {
      item.setChecked(true);
    }
  }

  void autoSaveTodoList() {
    if (userTodoListPath != null) {
      Path path = Paths.get(System.getProperty("user.home"), userTodoListPath);
      try (Writer writer = new FileWriter(path.toFile(), StandardCharsets.UTF_8)) {
        mapper.writerWithDefaultPrettyPrinter().writeValue(writer, todoList);
      } catch (IOException e) {
        System.err.println("Fikk ikke skrevet til todolist.json på hjemmeområdet");
      }
    }
  }
}
