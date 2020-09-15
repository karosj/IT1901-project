package todolist.ui;

import javafx.scene.control.ListCell;
import todolist.core.TodoItem;

public class TodoItemListCell extends ListCell<TodoItem> {

  @Override
  protected void updateItem(TodoItem item, boolean empty) {
    super.updateItem(item, empty);
    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    } else {
      setText((item.isChecked() ? "v" : "-") + " " + item.getText());
      setGraphic(null);
    }
  }
}
