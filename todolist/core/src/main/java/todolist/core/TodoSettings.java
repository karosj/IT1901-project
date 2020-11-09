package todolist.core;

public class TodoSettings {

  public enum TodoItemsSortOrder {
    NONE, UNCHECKED_CHECKED, CHECKED_UNCHECKED
  }

  private TodoItemsSortOrder todoItemSortOrder = TodoItemsSortOrder.UNCHECKED_CHECKED;

  public TodoItemsSortOrder getTodoItemSortOrder() {
    return todoItemSortOrder;
  }

  public void setTodoItemSortOrder(TodoItemsSortOrder todoItemSortOrder) {
    this.todoItemSortOrder = todoItemSortOrder;
  }
}
