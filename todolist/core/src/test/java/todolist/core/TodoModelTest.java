package todolist.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import todolist.core.TodoSettings.TodoItemsSortOrder;

public class TodoModelTest {

  private TodoModel todoModel;
  private TodoList list1, list2;

  @BeforeEach
  public void setUp() {
    todoModel = new TodoModel();
    list1 = new TodoList("todo1");
    list2 = new TodoList("todo2");
  }

  private void checkTodoLists(TodoList... lists) {
    Iterator<AbstractTodoList> it = todoModel.iterator();
    int pos = 0;
    while (it.hasNext()) {
      assertTrue(pos < lists.length);
      assertSame(lists[pos], it.next());
      pos++;
    }
    assertTrue(pos == lists.length);
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "\t"})
  public void testIsValidTodoListName_invalid(String invalid) {
    assertFalse(todoModel.isValidTodoListName(invalid));
  }

  @Test
  public void testIsValidTodoListName() {
    assertTrue(todoModel.isValidTodoListName("list1"));
    assertTrue(todoModel.isValidTodoListName(list1.getName()));
  }

  @Test
  public void testAddTodoList() {
    assertFalse(todoModel.iterator().hasNext());
    todoModel.addTodoList(list1);
    checkTodoLists(list1);
    todoModel.addTodoList(list2);
    checkTodoLists(list1, list2);
  }

  @Test
  public void testPutTodoList() {
    assertFalse(todoModel.iterator().hasNext());
    assertNull(todoModel.putTodoList(list1));
    checkTodoLists(list1);
    assertNull(todoModel.putTodoList(list2));
    checkTodoLists(list1, list2);
    TodoList list12 = new TodoList(list1.getName());
    assertSame(list1, todoModel.putTodoList(list12));
    checkTodoLists(list12, list2);
  }

  private void testTodoItemSortOrder(TodoItemsSortOrder sortOrder, List<TodoItem> todoItems, Integer... sortedTodoItemIndices) {
    TodoList todoList = new TodoList("todos");
    todoList.addTodoItems(todoItems.toArray(new TodoItem[todoItems.size()]));
    List<TodoItem> allAddedItems = new ArrayList<>(todoList.getTodoItems());
    Collection<TodoItem> expectedOrder = Stream.of(sortedTodoItemIndices).map(allAddedItems::get).collect(Collectors.toList());
    TodoListTest.checkItems(TodoModel.getSortedTodoItemsProvider(sortOrder).apply(todoList), expectedOrder.toArray(new TodoItem[expectedOrder.size()]));
    todoModel.getSettings().setTodoItemsSortOrder(sortOrder);
    TodoListTest.checkItems(todoModel.getSortedTodoItemsProvider().apply(todoList), expectedOrder.toArray(new TodoItem[expectedOrder.size()]));
  }

  @Test
  public void testTodoItemsSortOrder_NONE() {
    testTodoItemSortOrder(TodoItemsSortOrder.NONE, List.of(
        new TodoItem().text("item1").checked(false),
        new TodoItem().text("item2").checked(true),
        new TodoItem().text("item3").checked(false)
      ), 0, 1, 2);
  }

  @Test
  public void testTodoItemsSortOrder_UNCHECKED_CHECKED() {
    testTodoItemSortOrder(TodoItemsSortOrder.UNCHECKED_CHECKED, List.of(
        new TodoItem().text("item1").checked(false),
        new TodoItem().text("item2").checked(true),
        new TodoItem().text("item3").checked(false)
      ), 0, 2, 1);
  }

  @Test
  public void testTodoItemsSortOrder_CHECKED_UNCHECKED() {
    testTodoItemSortOrder(TodoItemsSortOrder.CHECKED_UNCHECKED, List.of(
        new TodoItem().text("item1").checked(false),
        new TodoItem().text("item2").checked(true),
        new TodoItem().text("item3").checked(false)
      ), 1, 0, 2);
  }
}
