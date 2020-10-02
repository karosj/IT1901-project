package todolist.ui;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import todolist.core.TodoItem;
import todolist.core.TodoList;
import todolist.core.TodoListListener;
import todolist.core.TodoModel;
import todolist.json.TodoPersistence;

public class TodoController {

  private static final String todoListWithTwoItems =
      "{\"lists\":[{\"name\":\"todo\",\"items\":[{\"text\":\"item1\",\"checked\":false},{\"text\":\"item2\",\"checked\":true,\"deadline\":\"2020-10-01T14:53:11\"}]}]}";

  private TodoModel todoModel;

  private TodoPersistence todoPersistence = new TodoPersistence();

  @FXML
  String userTodoListPath;

  @FXML
  String sampleTodoListResource;

  @FXML
  ComboBox<TodoList> todoListsView;

  @FXML
  TextField newTodoItemText;

  @FXML
  ListView<TodoItem> todoItemsView;

  @FXML
  Button deleteTodoItemButton;

  private void initializeTodoModel() {
    // setter opp data
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
      todoModel = todoPersistence.readTodoModel(reader);
    } catch (IOException e) {
      todoModel = new TodoModel();
      TodoList todoList = new TodoList(
        new TodoItem().text("Øl"),
         new TodoItem().text("Pizza")
      );
      todoModel.addTodoList(todoList);
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
    initializeTodoModel();
    selectionButtons = List.of(deleteTodoItemButton);
    // kobler data til list-controll
    initializeTodoListsView();
    initializeTodoItemsView();
    updateTodoListsView(null);
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
        return todoList.getName();
      }
      @Override
      public TodoList fromString(String newName) {
        TodoList todoList = new TodoList(); // getTodoList()
        todoList.setName(newName);
        return todoList;
      }
    });
    todoListsView.setEditable(true);
    todoListsView.valueProperty().addListener((prop, oldTodoList, newTodoList) -> {
      // must identify the case where newTodoList represents an edited name
      if (oldTodoList != null && newTodoList != null && (! todoListsView.getItems().contains(newTodoList))) {
        // either new name of dummy item or existing item
        if (oldTodoList.getName() == null) {
          // add as new list
          todoModel.addTodoList(newTodoList);
          updateTodoListsView(newTodoList);
        } else {
          // update name
          oldTodoList.setName(newTodoList.getName());
          updateTodoListsView(oldTodoList);
        }
      }
    });
    todoListsView.getSelectionModel().selectedItemProperty().addListener((prop, oldTodoList, newTodoList) -> {
      updateTodoItemsView();
    });
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
    System.out.println("TodoLists: " + todoListsView.getItems());
  }

  private TodoListListener todoListListener = todoList -> {
    autoSaveTodoList();
    updateTodoItemsView();
  };
  
  // makes class more testable
  TodoList getSelectedTodoList() {
    return todoListsView.getValue();
  }

  private void initializeTodoItemsView() {
    TodoItemListCellDragHandler dragHandler = new TodoItemListCellDragHandler(getSelectedTodoList());
    todoItemsView.setCellFactory(listView -> {
      TodoItemListCell listCell = new TodoItemListCell();
      dragHandler.registerHandlers(listCell);
      return listCell;
    });
    todoItemsView.getSelectionModel().selectedItemProperty()
        .addListener((prop, oldValue, newValue) -> updateTodoListButtons());
    todoItemsView.setEditable(true);
  }

  private TodoList currenTodoList = null;

  protected void updateTodoItemsView() {
    if (currenTodoList != null) {
      currenTodoList.removeTodoListListener(todoListListener);
    }
    List<TodoItem> items = new ArrayList<>();
    currenTodoList = getSelectedTodoList();
    if (currenTodoList != null) {
      items.addAll(getSelectedTodoList().getUncheckedTodoItems());
      items.addAll(getSelectedTodoList().getCheckedTodoItems());
    }
    TodoItem selectedItem = todoItemsView.getSelectionModel().getSelectedItem();
    todoItemsView.getItems().setAll(items);
    if (currenTodoList != null) {
      currenTodoList.addTodoListListener(todoListListener);
    }
    // keep selection
    if (selectedItem != null) {
      todoItemsView.getSelectionModel().select(selectedItem);
    }
    newTodoItemText.setText(null);
  }

  private void updateTodoListButtons() {
    boolean disable = todoItemsView.getSelectionModel().getSelectedItem() == null;
    for (Button button : selectionButtons) {
      button.setDisable(disable);
    }
    // TODO in progress...
    getRowLayoutY(todoItemsView, listCell -> isSelected(todoItemsView, listCell), 0);
    // System.out.println(rowLayoutY);
  }

  private boolean isSelected(ListView<?> listView, ListCell<?> listCell) {
    return isSelected(listView, listCell.getItem());
  }

  private boolean isSelected(ListView<?> listView, Object item) {
    return todoItemsView.getSelectionModel().getSelectedItems().contains(item);
  }

  @SuppressWarnings("unchecked")
  private <T> double getRowLayoutY(ListView<T> listView, Predicate<ListCell<T>> test, int num) {
    for (Node child : listView.lookupAll(".list-cell")) {
      if (child instanceof ListCell) {
        ListCell<T> listCell = (ListCell<T>) child;
        if (test.test(listCell) && num-- == 0) {
          double dy = 0;
          Node node = listCell;
          while (node != todoItemsView) {
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
    TodoItem item = getSelectedTodoList().createTodoItem();
    item.setText(newTodoItemText.getText());
    getSelectedTodoList().addTodoItem(item);
    todoItemsView.getSelectionModel().select(item);
  }

  @FXML
  void handleDeleteItemAction() {
    int index = todoItemsView.getSelectionModel().getSelectedIndex();
    TodoItem item = todoItemsView.getItems().get(index);
    if (item != null) {
      getSelectedTodoList().removeTodoItem(item);
      selectWithinBounds(index);
    }
  }

  private int selectWithinBounds(int index) {
    int maxIndex = todoItemsView.getItems().size() - 1;
    if (index > maxIndex) {
      index = maxIndex;
    }
    if (index >= 0) {
      todoItemsView.getSelectionModel().select(index);
      return index;
    }
    return -1;
  }

  @FXML
  void handleCheckItemAction() {
    TodoItem item = todoItemsView.getSelectionModel().getSelectedItem();
    if (item != null) {
      // toggle checked flag
      item.setChecked(! item.isChecked());
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
