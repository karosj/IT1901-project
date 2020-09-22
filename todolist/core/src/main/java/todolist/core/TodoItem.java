package todolist.core;

public class TodoItem {

  private String text;
  private boolean checked;

  @Override
  public String toString() {
    return String.format("[TodoItem text=%s checked=%s]", getText(), isChecked());
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public boolean isChecked() {
    return checked;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }

  public void set(TodoItem other) {
    this.checked = other.checked;
    this.text = other.text;
  }

  // fluent API

  public TodoItem checked(boolean checked) {
    setChecked(checked);
    return this;
  }

  public TodoItem text(String text) {
    setText(text);
    return this;
  }

  public TodoItem as(TodoItem other) {
    set(other);
    return this;
  }
}
