package todolist.core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Lists of items in a todo list.
 */
public class TodoList extends AbstractTodoList {

  private List<TodoItem> items = new ArrayList<>();

  public TodoList(String name, TodoItem... items) {
    super(name);
    addTodoItems(items);
  }

  @Override
  public String toString() {
    return String.format("[TodoList name=%s deadline=%s #items=%s]",
        getName(), getDeadline(), items.size());
  }

  @Override
  public TodoItem createTodoItem() {
    return new TodoListItem();
  }

  /**
   * Adds the provided TodoItems to this TodoList.
   * If a TodoItem is not an instance of TodoListItem,
   * its contents is copied in to a new TodoListItem and that is added instead.
   *
   * @param items the TodoItems to add
   * @throws IllegalStateException if an item is a TodoListItem not belonging to this TodoList
   */
  @Override
  public void addTodoItems(TodoItem... items) throws IllegalStateException {
    for (TodoItem item : items) {
      TodoListItem todoListItem = null;
      if (item instanceof TodoListItem tli) {
        todoListItem = tli;
      } else {
        todoListItem = new TodoListItem();
        todoListItem.setText(item.getText());
        todoListItem.setChecked(item.isChecked());
        todoListItem.setDeadline(item.getDeadline());
      }
      if (todoListItem.getTodoList() != this) {
        throw new IllegalStateException("TodoListItem does not belong to this list TodoList");
      }
      this.items.add(todoListItem);
    }
    fireTodoListChanged();
  }

  @Override
  public void removeTodoItem(TodoItem item) {
    items.remove(item);
    fireTodoListChanged();
  }

  @Override
  public Iterator<TodoItem> iterator() {
    return items.iterator();
  }

  @Override
  protected Collection<TodoItem> getTodoItems(Boolean checked) {
    Collection<TodoItem> result = new ArrayList<>(items.size());
    for (TodoItem item : items) {
      if (checked == null || item.isChecked() == checked) {
        result.add(item);
      }
    }
    return result;
  }

  @Override
  public Collection<TodoItem> getOverdueTodoItems() {
    return items.stream().filter(TodoItem::isOverdue).collect(Collectors.toList());
  }

  // index-oriented methods

  @Override
  public int indexOf(TodoItem item) {
    return items.indexOf(item);
  }

  /**
   * Moves the provided TodoItem to a new position given by newIndex.
   * Items in-betweem the old and new positions are shifted.
   *
   * @param item     the item to move
   * @param newIndex the new position
   */
  @Override
  public void moveTodoItem(TodoItem item, int newIndex) {
    items.remove(item);
    items.add(newIndex, item);
    fireTodoListChanged();
  }

  // støtte for lytting

  @Override
  protected void fireTodoListChanged(TodoListListener listener) {
    listener.todoListChanged(this);
  }

  private class TodoListItem extends TodoItem {

    @Override
    public String toString() {
      return String.format("[TodoItem text=%s checked=%s deadline=%s @ %s]", getText(), isChecked(),
          getDeadline(), items.indexOf(this));
    }
  
    TodoList getTodoList() {
      return TodoList.this;
    }
  
    @Override
    public void setText(String text) {
      if (! Objects.equals(text, getText())) {
        super.setText(text);
        fireTodoListChanged(this);
      }
    }
  
    @Override
    public void setChecked(boolean checked) {
      if (checked != isChecked()) {
        super.setChecked(checked);
        fireTodoListChanged(this);
      }
    }
  
    @Override
    public void setDeadline(LocalDateTime deadline) {
      if (! Objects.equals(deadline, getDeadline())) {
        super.setDeadline(deadline);
        fireTodoListChanged(this);
      }
    }
  
    @Override
    public void setAs(TodoItem other) {
      boolean equals = isChecked() == other.isChecked()
          && Objects.equals(getText(), other.getText())
          && Objects.equals(getDeadline(), other.getDeadline());
      if (! equals) {
        super.setAs(other);
        fireTodoListChanged(this);
      }
    }
  }
}
