package todolist.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TodoList implements Iterable<TodoItem> {

  private List<TodoItem> items = new ArrayList<>();

  public TodoItem createTodoItem() {
    return new TodoListItem(this);
  }

  /**
   * Adds the provided TodoItem to this TodoList.
   * If the TodoItem is not an instance of TodoListItem,
   * its contents is copied in to a new TodoListItem and that is added instead.
   *
   * @param item the TodoItem to add
   */
  public void addTodoItem(TodoItem item) {
    TodoListItem todoListItem = null;
    if (item instanceof TodoListItem) {
      todoListItem = (TodoListItem) item;
    } else {
      todoListItem = new TodoListItem(this);
      todoListItem.setText(item.getText());
      todoListItem.setChecked(item.isChecked());
    }
    items.add(todoListItem);
    fireTodoListChanged();
  }

  public void removeTodoItem(TodoItem item) {
    items.remove(item);
    fireTodoListChanged();
  }

  @Override
  public Iterator<TodoItem> iterator() {
    return items.iterator();
  }

  private Collection<TodoItem> getTodoItems(Boolean checked) {
    Collection<TodoItem> result = new ArrayList<>(items.size());
    for (TodoItem item : items) {
      if (checked == null || item.isChecked() == checked) {
        result.add(item);
      }
    }
    return result;
    // same as
    // return items.stream()
    //  .filter(item -> checked == null || item.isChecked() == checked)
    //  .collect(Collectors.toList());
  }

  public Collection<TodoItem> getTodoItems() {
    return getTodoItems(null);
  }

  public Collection<TodoItem> getCheckedTodoItems() {
    return getTodoItems(true);
  }

  public Collection<TodoItem> getUncheckedTodoItems() {
    return getTodoItems(false);
  }

  // støtte for lytting

  private Collection<TodoListListener> todoListListeners = new ArrayList<>();

  public void addTodoListListener(TodoListListener listener) {
    todoListListeners.add(listener);
  }

  public void removeTodoListListener(TodoListListener listener) {
    todoListListeners.remove(listener);
  }

  protected void fireTodoListChanged(TodoItem item) {
    fireTodoListChanged();
  }

  protected void fireTodoListChanged() {
    for (TodoListListener listener : todoListListeners) {
      listener.todoListChanged(this);
    }
  }
}
