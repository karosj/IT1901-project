package todolist.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todolist.core.TodoSettings.TodoItemsSortOrder;

public class TodoSettingsTest {
  
  private TodoSettings todoSettings;
  
  @BeforeEach
  public void setup() {
    todoSettings = new TodoSettings();
    todoSettings.setTodoItemSortOrder(TodoItemsSortOrder.NONE);
    todoSettings.applyChanges();
  }

  @Test
  public void testSetTodoItemsSortOrder_changesProperty() {
    todoSettings.setTodoItemSortOrder(TodoItemsSortOrder.CHECKED_UNCHECKED);
    assertEquals(TodoItemsSortOrder.CHECKED_UNCHECKED, todoSettings.getTodoItemSortOrder());
  }

  private static Collection<String> TODO_ITEMS_SORT_ORDER_SINGLETON_COLLECTION = Collections.singleton("todoItemSortOrder");

  @Test
  public void testSetTodoItemsSortOrder_todoSettingsChangedCalledWhenPropertyChanged() {
    TodoSettingsListener todoSettingsListener = mock(TodoSettingsListener.class);
    todoSettings.addTodoSettingsListener(todoSettingsListener);
    todoSettings.setTodoItemSortOrder(TodoItemsSortOrder.CHECKED_UNCHECKED);
    assertEquals(TodoItemsSortOrder.CHECKED_UNCHECKED, todoSettings.getTodoItemSortOrder());
    // listener called when property changed
    verify(todoSettingsListener, times(1)).todoSettingsChanged(todoSettings, TODO_ITEMS_SORT_ORDER_SINGLETON_COLLECTION);
    todoSettings.setTodoItemSortOrder(TodoItemsSortOrder.CHECKED_UNCHECKED);
    assertEquals(TodoItemsSortOrder.CHECKED_UNCHECKED, todoSettings.getTodoItemSortOrder());
    // listener not called when property didn't change
    verify(todoSettingsListener, times(1)).todoSettingsChanged(todoSettings, TODO_ITEMS_SORT_ORDER_SINGLETON_COLLECTION);
    todoSettings.setTodoItemSortOrder(TodoItemsSortOrder.UNCHECKED_CHECKED);
    assertEquals(TodoItemsSortOrder.UNCHECKED_CHECKED, todoSettings.getTodoItemSortOrder());
    // listener called another time when property changed
    verify(todoSettingsListener, times(2)).todoSettingsChanged(todoSettings, TODO_ITEMS_SORT_ORDER_SINGLETON_COLLECTION);
  }

  @Test
  public void testSetTodoItemsSortOrder_todoSettingsChangedNotCalledWhenPropertyNotChanged() {
    TodoSettingsListener todoSettingsListener = mock(TodoSettingsListener.class);
    todoSettings.addTodoSettingsListener(todoSettingsListener);
    todoSettings.setTodoItemSortOrder(TodoItemsSortOrder.NONE);
    verifyNoInteractions(todoSettingsListener);
  }

  @Test
  public void testSetTodoItemsSortOrder_applyChanges() {
    TodoSettingsListener todoSettingsListener = mock(TodoSettingsListener.class);
    todoSettings.addTodoSettingsListener(todoSettingsListener);
    todoSettings.setTodoItemSortOrder(TodoItemsSortOrder.CHECKED_UNCHECKED);
    // listener called when property changed
    verify(todoSettingsListener, times(1)).todoSettingsChanged(todoSettings, TODO_ITEMS_SORT_ORDER_SINGLETON_COLLECTION);
    todoSettings.applyChanges();
    // apply changes not canceled
    assertEquals(TodoItemsSortOrder.CHECKED_UNCHECKED, todoSettings.getTodoItemSortOrder());
  }

  @Test
  public void testSetTodoItemsSortOrder_cancelChanges() {
    TodoSettingsListener todoSettingsListener = mock(TodoSettingsListener.class);
    todoSettings.addTodoSettingsListener(todoSettingsListener);
    todoSettings.setTodoItemSortOrder(TodoItemsSortOrder.CHECKED_UNCHECKED);
    todoSettings.cancelChanges();
    // property change(s) rolled back
    assertEquals(TodoItemsSortOrder.NONE, todoSettings.getTodoItemSortOrder());
  }

  @Test
  public void testSetTodoItemsSortOrder_todoSettingChangedCanceled() {
    TodoSettingsListener todoSettingsListener = mock(TodoSettingsListener.class);
    doThrow(IllegalStateException.class).when(todoSettingsListener).todoSettingsChanged(todoSettings, TODO_ITEMS_SORT_ORDER_SINGLETON_COLLECTION);
    todoSettings.addTodoSettingsListener(todoSettingsListener);
    todoSettings.setTodoItemSortOrder(TodoItemsSortOrder.CHECKED_UNCHECKED);
    // property change rolled back
    assertEquals(TodoItemsSortOrder.NONE, todoSettings.getTodoItemSortOrder());
    verify(todoSettingsListener, times(2)).todoSettingsChanged(todoSettings, TODO_ITEMS_SORT_ORDER_SINGLETON_COLLECTION);
  }
}
