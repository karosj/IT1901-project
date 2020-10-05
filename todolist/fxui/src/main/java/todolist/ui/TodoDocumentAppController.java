package todolist.ui;

import fxutil.doc.DocumentListener;
import fxutil.doc.FileMenuController;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;
import javafx.fxml.FXML;
import todolist.core.TodoModel;

public class TodoDocumentAppController implements DocumentListener<TodoModel, File> {

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
  String userAppConfigPath;

  @FXML
  FileMenuController fileMenuController;

  @FXML
  TodoModelController todoModelViewController;

  /**
   * Map of config data. Current contents:
   * 
   * fileMenu.recentFiles = [ ... ]
   */
  private TomlParseResult config;

  @FXML
  private void initialize() {
    fileMenuController.setDocumentStorage(todoModelStorage);
    todoModelStorage.addDocumentStorageListener(this);
    applyConfig();
    if (! fileMenuController.openMostRecentFile()) {
      todoModelStorage.newDocument();
    }
  }

  private void applyConfig() {
    if (userAppConfigPath != null) {
      try {
        Path configPath = Paths.get(System.getProperty("user.home"), userAppConfigPath);
        config = Toml.parse(configPath);
      } catch (IOException ioex) {
        System.err.println("Fant ingen " + userAppConfigPath + " på hjemmeområdet");
      }
    }
    if (config == null) {
      try {
        config = Toml.parse(getClass().getResourceAsStream("todo-config.toml"));
      } catch (IOException e) {
        // ignore
      }
    }
    if (config != null) {
      if (config.contains("fileMenu.recentFiles")) {
        List<File> recentFiles = config.getArray("fileMenu.recentFiles").toList().stream()
            .map(o -> new File(o.toString())).collect(Collectors.toList());
        fileMenuController.addRecentFiles(recentFiles.toArray(new File[recentFiles.size()]));
      }
    }
  }

  void writeConfig() {
    // TODO
    Path configPath = Paths.get(System.getProperty("user.home"), userAppConfigPath);
    try (FileWriter writer = new FileWriter(configPath.toFile())) {
      writer.write("[fileMenu]\n");
      writer.write("recentFiles = [ ");
      writer.write(fileMenuController.getRecentFiles().stream()
          .map(o -> "\"" + o + "\"")
          .collect(Collectors.joining(", ")));
      writer.write(" ]\n");
    } catch(IOException ioe) {
      System.out.println("Fikk ikke skrevet konfigurasjon til " + userAppConfigPath + " på hjemmeområdet");
    }
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
