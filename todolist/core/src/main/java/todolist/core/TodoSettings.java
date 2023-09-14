package todolist.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A class for managing settings,
 * i.e. properties that control behavior of other classes.
 * Changes to settings can be listened to and vetoed by throwing an exception.
 */
public class TodoSettings {

  private Collection<TodoSettingsListener> todoSettingsListeners = new ArrayList<>();

  public void addTodoSettingsListener(TodoSettingsListener listener) {
    todoSettingsListeners.add(listener);
  }

  public void removeTodoSettingsListener(TodoSettingsListener listener) {
    todoSettingsListeners.remove(listener);
  }

  private Map<String, Object> oldValues = null;

  private void handleSettingChanged(String property, Object oldValue, Object newValue) {
    if (Objects.equals(oldValue, newValue)) {
      return;
    }
    if (oldValues == null) {
      oldValues = new HashMap<>();
    }
    if (! oldValues.containsKey(property)) {
      oldValues.put(property, oldValue);
    } else if (Objects.equals(newValue, oldValues.get(property))) {
      oldValues.remove(property);
    }
    try {
      fireSettingsChanged(Collections.singleton(property));
    } catch (RuntimeException re) {
      cancelChange(property);
    }
  }

  private void fireSettingsChanged(Collection<String> changedProperties) {
    for (TodoSettingsListener listener : todoSettingsListeners) {
      listener.todoSettingsChanged(this, changedProperties);
    }
  }

  /**
   * Call to indicate that new values will be used, and
   * old values can be forgotten.
   */
  public void applyChanges() {
    oldValues = null;
  }

  private void cancelChange(String property) {
    var oldValue = oldValues.get(property);
    var setterName = "set" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
    try {
      var setter = getClass().getMethod(setterName, oldValue.getClass());
      setter.invoke(this, oldValue);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      // ignore
    }
  }

  /**
   * Call to rollback changes to old values.
   */
  public void cancelChanges() {
    try {
      for (String property : oldValues.keySet()) {
        cancelChange(property);
      }
    } finally {
      oldValues = null;
    }
  }

  //

  /**
   * Enum for possible values of sort order for items.
   */
  public enum TodoItemsSortOrder {
    NONE, UNCHECKED_CHECKED, CHECKED_UNCHECKED
  }

  private TodoItemsSortOrder todoItemsSortOrder = TodoItemsSortOrder.NONE;

  public TodoItemsSortOrder getTodoItemsSortOrder() {
    return todoItemsSortOrder;
  }

  // settings name must correspond to setter name!
  public static final String TODO_ITEM_SORT_ORDER_SETTING = "todoItemsSortOrder";

  /**
   * Sets the todoItemsSortOrder property, and notifies listeners.
   *
   * @param todoItemSortOrder the new todoItemSortOrder value
   */
  public void setTodoItemsSortOrder(TodoItemsSortOrder todoItemSortOrder) {
    Object oldValue = this.todoItemsSortOrder;
    this.todoItemsSortOrder = todoItemSortOrder;
    handleSettingChanged(TODO_ITEM_SORT_ORDER_SETTING, oldValue, todoItemSortOrder);
  }
}
