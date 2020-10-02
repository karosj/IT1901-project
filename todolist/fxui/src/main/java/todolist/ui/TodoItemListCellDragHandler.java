package todolist.ui;

import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import todolist.core.TodoItem;
import todolist.core.TodoList;

public class TodoItemListCellDragHandler {

  private TodoList todoList;

  public void setTodoList(TodoList todoList) {
    this.todoList = todoList;
  }

  /**
   * Register this handler for the provided TodoItemListCell. Will attach dragDetected, dragOver and
   * dragDropped handlers.
   *
   * @param listCell the TodoItemListCell
   */
  public void registerHandlers(TodoItemListCell listCell) {
    listCell.setOnDragDetected(this::handleDragStart);
    listCell.setOnDragOver(this::handleDragOver);
    listCell.setOnDragDropped(this::handleDragEnd);
  }

  private void handleDragStart(MouseEvent event) {
    if (event.getSource() instanceof TodoItemListCell) {
      TodoItemListCell listCell = (TodoItemListCell) event.getSource();
      if (!listCell.isEmpty()) {
        Dragboard dragboard = listCell.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(listCell.getItem().getText());
        dragboard.setContent(content);
        event.consume();
      }
    }
  }

  private void handleDragOver(DragEvent event) {
    if (event.getGestureSource() instanceof TodoItemListCell
        && event.getSource() instanceof TodoItemListCell) {
      TodoItem sourceItem = ((TodoItemListCell) event.getGestureSource()).getItem();
      TodoItemListCell targetCell = (TodoItemListCell) event.getSource();
      if ((!targetCell.isEmpty()) && acceptDrop(sourceItem, targetCell.getItem())) {
        event.acceptTransferModes(TransferMode.MOVE);
        // TODO: give feedback that drop will be accepted
      }
    }
    event.consume();
  }

  private boolean acceptDrop(TodoItem sourceItem, TodoItem targetItem) {
    return todoList.indexOf(sourceItem) >= 0 && sourceItem.isChecked() == targetItem.isChecked();
  }

  private void handleDragEnd(DragEvent event) {
    boolean success = false;
    if (event.getGestureSource() instanceof TodoItemListCell
        && event.getGestureTarget() instanceof TodoItemListCell) {
      TodoItem sourceItem = ((TodoItemListCell) event.getGestureSource()).getItem();
      TodoItemListCell targetCell = (TodoItemListCell) event.getGestureTarget();
      if ((!targetCell.isEmpty()) && acceptDrop(sourceItem, targetCell.getItem())) {
        int newIndex = todoList.indexOf(targetCell.getItem());
        if (newIndex >= 0) {
          todoList.moveTodoItem(sourceItem, newIndex);
          success = true;
        }
      }
    }
    event.setDropCompleted(success);
    event.consume();
  }
}
