package todolist.core;

public class TodoListItem extends TodoItem {

  private final TodoList todoList;

  public TodoListItem(TodoList todoList) {
    this.todoList = todoList;
  }

  @Override
  public void setText(String text) {
    String oldText = getText();
    super.setText(text);
    if (oldText != text || oldText != null && !(oldText.equals(text))) {
      todoList.fireTodoListChanged(this);
    }
  }

  @Override
  public void setChecked(boolean checked) {
    boolean oldChecked = isChecked();
    super.setChecked(checked);
    if (oldChecked != checked) {
      todoList.fireTodoListChanged(this);
    }
  }
}
