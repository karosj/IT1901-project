package todolist.ui;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import todolist.core.TodoItem;
import todolist.core.TodoList;
import todolist.core.TodoModel;
import todolist.json.TodoPersistence;

public class TodoAppController {

  private static final String todoListWithTwoItems =
      "{\"lists\":["
        + "{\"name\":\"todo\",\"items\":[{\"text\":\"item1\",\"checked\":false},"
        + "{\"text\":\"item2\",\"checked\":true,\"deadline\":\"2020-10-01T14:53:11\"}]}"
      + "]}";

  @FXML
  String userTodoModelPath;
  
  @FXML
  String sampleTodoModelResource;
  
  @FXML
  TodoModelController todoModelViewController;
  
  private TodoModel getInitialTodoModel() {
    // setter opp data
    Reader reader = null;
    // try to read file from home folder first
    if (userTodoModelPath != null) {
      try {
        reader = new FileReader(Paths.get(System.getProperty("user.home"), userTodoModelPath)
            .toFile(), StandardCharsets.UTF_8);
      } catch (IOException ioex) {
        System.err.println("Fant ingen " + userTodoModelPath + " på hjemmeområdet");
      }
    }
    if (reader == null && sampleTodoModelResource != null) {
      // try sample todo list from resources instead
      URL url = getClass().getResource(sampleTodoModelResource);
      if (url != null) {
        try {
          reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
          System.err.println("Kunne ikke lese innebygget " + sampleTodoModelResource);
        }
      } else {
        System.err.println("Fant ikke innebygget " + sampleTodoModelResource);
      }
    }
    if (reader == null) {
      // use embedded String
      reader = new StringReader(todoListWithTwoItems);
    }
    TodoModel todoModel = null;
    try {
      TodoPersistence todoPersistence = new TodoPersistence();
      todoModel = todoPersistence.readTodoModel(reader);
    } catch (IOException e) {
      // ignore
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        // ignore
      }
    }
    if (todoModel == null) {
      todoModel = new TodoModel();
      TodoList todoList = new TodoList(
          new TodoItem().text("Øl"),
          new TodoItem().text("Pizza")
        );
      todoModel.addTodoList(todoList);
    }
    return todoModel;
  }

  @FXML
  void initialize() {
    todoModelViewController.setTodoModel(getInitialTodoModel());
  }
}
