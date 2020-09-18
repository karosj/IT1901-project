package todolist.ui;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import todolist.core.TodoItem;

public class TodoItemListCell extends ListCell<TodoItem> {

  private CheckBox checkedView = new CheckBox();
  private ChangeListener<Boolean> checkedViewListener = null;

  @Override
  protected void updateItem(TodoItem item, boolean empty) {
    super.updateItem(item, empty);
    setText(null);
    if (empty || item == null) {
      setGraphic(null);
    } else {
      String text = item.getText();
      checkedView.setText(text);
      checkedView.setSelected(item.isChecked());
      if (checkedViewListener == null) {
        checkedViewListener = (prop, oldValue, newValue) -> {
          getItem().setChecked(checkedView.isSelected());
        };
        checkedView.selectedProperty().addListener(checkedViewListener);
      }
      setGraphic(checkedView);
    }
  }
}
