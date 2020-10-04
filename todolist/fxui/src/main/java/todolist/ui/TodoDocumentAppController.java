package todolist.ui;

import fxutil.doc.FileMenuController;
import fxutil.doc.DocumentListener;
import java.io.File;
import javafx.fxml.FXML;
import todolist.core.TodoModel;

public class TodoDocumentAppController extends FileMenuController implements DocumentListener<TodoModel, File> {

  private final TodoModelStorage todoModelStorage;

  public TodoDocumentAppController() {
    todoModelStorage = new TodoModelStorage();
    todoModelStorage.addDocumentStorageListener(this);
  }

  public TodoModel getTodoModel() {
    return todoModelStorage.getDocument();
  }

  // to make it testable
  void setTodoModel(final TodoModel todoModel) {
    todoModelStorage.setDocument(todoModel);
    todoModelViewController.setTodoModel(getTodoModel());
  }

  @FXML
  String userTodoModelPath;
  
  @FXML
  String sampleTodoModelResource;
  
  @FXML
  TodoModelController todoModelViewController;

  @FXML
  private void initialize() {
    setDocumentStorage(todoModelStorage);
  }

  // DocumentListener

  @Override
  public void documentLocationChanged(final File documentLocation, final File oldDocumentLocation) {
  }

  @Override
  public void documentChanged(final TodoModel document, final TodoModel oldDocument) {
    todoModelViewController.setTodoModel(getTodoModel());
  }
}
