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
  
  public void updateWith(TodoItem other) {
    boolean oldChecked = isChecked();
    String oldText = getText();
    super.updateWith(other);
    if (oldChecked != other.isChecked() || oldText != other.getText() || oldText != null && !(oldText.equals(other.getText()))) {
      todoList.fireTodoListChanged(this);
    }
  }
}
