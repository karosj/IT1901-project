package todolist.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
import org.testfx.util.WaitForAsyncUtils;
import todolist.core.TodoItem;
import todolist.core.TodoList;
import todolist.core.TodoModel;
import todolist.json.TodoPersistence;

public class TodoListControllerTest extends ApplicationTest {

  private TodoListController controller;

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("TodoList_test.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

  private TodoPersistence todoPersistence = new TodoPersistence();
  private TodoList todoList;
  private TodoItem item1, item2;

  @BeforeEach
  public void setupItems() {
    TodoModel todoModel = null;
    try {
      todoModel =
          todoPersistence.readTodoModel(new InputStreamReader(getClass().getResourceAsStream("test-todomodel.json")));
    } catch (IOException e) {
      fail("Couldn't load test-todomodel.json");
    }
    assertNotNull(todoModel);
    assertTrue(todoModel.iterator().hasNext());
    this.todoList = todoModel.iterator().next();
    this.controller.setTodoList(this.todoList);
    // same as in test-todomodel.json
    Iterator<TodoItem> todoItems = todoList.iterator();
    item1 = new TodoItem().as(todoItems.next());
    item2 = new TodoItem().as(todoItems.next());
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
    final ListView<TodoItem> todoListView = lookup("#todoItemsView").query();
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
    // check element is selected
    checkSelectedTodoItem(1);
  }

  @Test
  public void testDeleteTodoItem() {
    // final ListView<TodoItem> todoListView = lookup("#todoItemsView").query();
    // todoListView.getSelectionModel().select(1);
    clickOn(findTodoItemListCellNode(cell -> true, ".label", 1));
    clickOn("#deleteTodoItemButton");
    // item2 is removed, only item1 is left
    checkTodoListItems(item1);
    // item2 is removed, only item1 is left
    checkTodoListViewItems(item1);
    // check remaining item is selected
    checkSelectedTodoItem(0);
  }

  @Test
  public void testCheckTodoItemListCell() {
    clickOn(findTodoItemListCellNode(cell -> !cell.getItem().isChecked(), ".check-box", 0));
    TodoItem newItem2 = item2.withChecked(true);
    // item is changed
    checkTodoListItems(item1, newItem2);
    // items in list view change order
    checkTodoListViewItems(item1, newItem2);
  }

  @Test
  public void testDragTodoItem() {
    Predicate<TodoItemListCell> draggableCell = cell -> cell.lookup(".label") != null;
    TodoItemListCell sourceTodoItemListCell = findTodoItemListCell(draggableCell, 0);
    TodoItemListCell targetTodoItemListCell = findTodoItemListCell(draggableCell, 1);
    drag(sourceTodoItemListCell).dropTo(targetTodoItemListCell);
    // item order is changed
    checkTodoListItems(item2, item1);
    // items in list view do not change order
    checkTodoListViewItems(item1, item2);
  }

  // utility methods

  private Node findNode(Predicate<Node> nodeTest, int num) {
    int count = 0;
    for (Node node : lookup(nodeTest).queryAll()) {
      if (nodeTest.test(node) && count++ == num) {
        return node;
      }
    }
    return null;
  }

  private Node waitForNode(Predicate<Node> nodeTest, int num) {
    Node[] nodes = new Node[1];
    try {
      WaitForAsyncUtils.waitFor(500, TimeUnit.MILLISECONDS,
          () -> {
            while (true) {
              if ((nodes[0] = findNode(nodeTest, num)) != null) {
                return true;
              }
              Thread.sleep(10);
            }
          }
      );
    } catch (TimeoutException e) {
      fail("No appropriate node available");
    }
    return nodes[0];
  }

  private TodoItemListCell findTodoItemListCell(Predicate<TodoItemListCell> test, int num) {
    return (TodoItemListCell) waitForNode(node -> node instanceof TodoItemListCell && test.test((TodoItemListCell) node), num);
  }

  private Node findTodoItemListCellNode(Predicate<TodoItemListCell> test, String selector, int num) {
    Node listCell = waitForNode(node -> node instanceof TodoItemListCell && (selector == null || node.lookup(selector) != null) && test.test((TodoItemListCell) node), num);
    return listCell.lookup(selector);
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
    ListView<TodoItem> todoListView = lookup("#todoItemsView").query();
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

  private void checkSelectedTodoItem(int index) {
    final ListView<TodoItem> todoListView = lookup("#todoItemsView").query();
    assertEquals(index, todoListView.getSelectionModel().getSelectedIndex());
  }
}
