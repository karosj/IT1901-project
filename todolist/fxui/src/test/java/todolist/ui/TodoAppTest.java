package todolist.ui;

import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import todolist.core.TodoItem;
import todolist.core.TodoList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class TodoAppTest extends ApplicationTest {

  private TodoController controller;
  private TodoList todoList;

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("TodoTest.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    this.todoList = this.controller.getTodoList();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Test
  public void testController() {
    assertNotNull(this.controller);
    assertNotNull(this.todoList);
    assertEquals(2, this.todoList.getTodoItems().size());
  }

  @Test
  public void testTodoListView_initialItems() {
    final ListView<TodoItem> todoListView = lookup("#todoListView").query();
    // list contains same set of elements as the todo list (in some order)
    Set<TodoItem> listViewItems = new HashSet<>(todoListView.getItems());
    Set<TodoItem> todoListItems = new HashSet<>(this.todoList.getTodoItems());
    assertEquals(todoListItems, listViewItems);
  }

  @Test
  public void testNewTodoItem() {
    Set<TodoItem> todoListItems1 = new HashSet<>(this.todoList.getTodoItems());
    clickOn("#newTodoItemText").write("New item");
    clickOn("#newTodoItemButton");
    Set<TodoItem> todoListItems2 = new HashSet<>(this.todoList.getTodoItems());
    todoListItems2.removeAll(todoListItems1);
    // one new item
    assertEquals(1, todoListItems2.size());
  }
}
