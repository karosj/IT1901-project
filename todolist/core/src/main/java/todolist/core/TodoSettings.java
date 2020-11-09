package todolist.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TodoSettings {

  private Collection<TodoSettingsListener> todoSettingsListeners = new ArrayList<>();

  public void addTodoSettingsListener(TodoSettingsListener listener) {
    todoSettingsListeners.add(listener);
  }

  public void removeTodoSettingsListener(TodoSettingsListener listener) {
    todoSettingsListeners.remove(listener);
  }

  private Map<String, Object> oldValues = null;

  private void fireSettingChanged(String property, Object oldValue, Object newValue) {
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

  public void applyChanges() {
    oldValues = null;
  }

  private void cancelChange(String property) {
    Object oldValue = oldValues.get(property);
    try {
      Method setter = getClass().getMethod("set" + Character.toUpperCase(property.charAt(0)) + property.substring(1),
          oldValue.getClass());
      setter.invoke(this, oldValue);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      // ignore
    }
  }

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

  public enum TodoItemsSortOrder {
    NONE, UNCHECKED_CHECKED, CHECKED_UNCHECKED
  }

  private TodoItemsSortOrder todoItemSortOrder = TodoItemsSortOrder.UNCHECKED_CHECKED;

  public TodoItemsSortOrder getTodoItemSortOrder() {
    return todoItemSortOrder;
  }

  public void setTodoItemSortOrder(TodoItemsSortOrder todoItemSortOrder) {
    Object oldValue = this.todoItemSortOrder;
    this.todoItemSortOrder = todoItemSortOrder;
    fireSettingChanged("todoItemSortOrder", oldValue, todoItemSortOrder);
  }
}
