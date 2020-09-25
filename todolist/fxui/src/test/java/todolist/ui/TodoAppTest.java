package todolist.ui;

import java.util.function.Predicate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import todolist.core.TodoItem;
import todolist.core.TodoList;

public class TodoAppTest extends ApplicationTest {

  private TodoController controller;
  private TodoList todoList;
  private TodoItem item1, item2;

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("TodoTest.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    this.todoList = this.controller.getTodoList();
    stage.setScene(new Scene(root));
    stage.show();
  }
  
  @BeforeEach
  public void setupItems() {
    // same as in test-todolist.json (should perhaps read it instead)
    item1 = new TodoItem().checked(true).text("Item 1");
    item2 = new TodoItem().checked(false).text("Item 2");
  }

  @Test
  public void testController_todoList() {
    assertNotNull(this.controller);
    assertNotNull(this.todoList);
    // initial todo items
    checkTodoItems(this.todoList, item1, item2);
  }

  @Test
  public void testTodoListView_initialItems() {
    final ListView<TodoItem> todoListView = lookup("#todoListView").query();
    // initial todo items, note the unchecked one comes first
    checkTodoItems(todoListView.getItems(), item2, item1);
  }
  
  @Test
  public void testNewTodoItem() {
    String newItemText = "New item";
    clickOn("#newTodoItemText").write(newItemText);
    clickOn("#newTodoItemButton");
    TodoItem newItem = new TodoItem().text(newItemText);
    // item is added last in underlying todo list
    checkTodoListItems(item1, item2, newItem);
    // item is last of the unchecked items in list view
    checkTodoListViewItems(item2, newItem, item1);
  }

  @Test
  public void testDeleteTodoItem() {
    // final ListView<TodoItem> todoListView = lookup("#todoListView").query();
    // todoListView.getSelectionModel().select(1);
    TodoItemListCell todoItemListCell = findTodoItemListCell(1);
    clickOn(todoItemListCell.lookup(".label"));

    clickOn("#deleteTodoItemButton");
    // item2 is removed, only item1 is left
    checkTodoListItems(item1);
    // item2 is removed, only item1 is left
    checkTodoListViewItems(item1);
  }

  @Test
  public void testCheckTodoItem() {
    TodoItemListCell todoItemListCell = findTodoItemListCell(cell -> ! cell.getItem().isChecked());
    clickOn(todoItemListCell.lookup(".check-box"));
    TodoItem newItem2 = item2.withChecked(true);
    // item is changed
    checkTodoListItems(item1, newItem2);
    // items in list view change order
    checkTodoListViewItems(item1, newItem2);
  }

  @Test
  public void testDragTodoItem() {
    TodoItemListCell sourceTodoItemListCell = findTodoItemListCell(0);
    TodoItemListCell targetTodoItemListCell = findTodoItemListCell(1);
    drag(sourceTodoItemListCell).dropTo(targetTodoItemListCell);
    // item order is changed
    checkTodoListItems(item2, item1);
    // items in list view do not change order
    checkTodoListViewItems(item1, item2);
  }

  // utility methods

  private TodoItemListCell findTodoItemListCell(int num) {
    return findTodoItemListCell(cell -> true, num);
  }

  private TodoItemListCell findTodoItemListCell(Predicate<TodoItemListCell> test) {
    return findTodoItemListCell(test, 0);
  }

  private TodoItemListCell findTodoItemListCell(Predicate<TodoItemListCell> test, int num) {
    for (Node node : lookup(".list-cell").queryAll()) {
      if (node instanceof TodoItemListCell) {
        TodoItemListCell todoItemListCell = (TodoItemListCell) node;
        if (test.test(todoItemListCell) && num-- == 0) {
          return todoItemListCell;
        }
      }
    }
    fail();
    return null;
  }

  private void checkTodoItem(TodoItem item, Boolean checked, String text) {
    if (checked != null) {
      assertEquals(checked, item.isChecked());
    }
    if (text != null) {
      assertEquals(text, item.getText());
    }
  }

  private void checkTodoListItems(TodoItem... items) {
    checkTodoItems(this.todoList, items);
  }

  private void checkTodoListViewItems(TodoItem... items) {
    final ListView<TodoItem> todoListView = lookup("#todoListView").query();
    checkTodoItems(todoListView.getItems(), items);
  }

  private void checkTodoItems(Iterable<TodoItem> it, TodoItem... items) {
    int i = 0;
    for (TodoItem item : items) {
      assertTrue(i < items.length);
      checkTodoItem(item, items[i].isChecked(), items[i].getText());
      i++;
    }
    assertTrue(i == items.length);
  }
}
