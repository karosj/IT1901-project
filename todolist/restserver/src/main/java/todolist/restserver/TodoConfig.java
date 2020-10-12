package todolist.restserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todolist.core.TodoList;
import todolist.core.TodoModel;
import todolist.json.TodoPersistence;
import todolist.restapi.TodoModelService;

public class TodoConfig extends ResourceConfig {

  private TodoModel todoModel;

  /**
   * Initialize this TodoConfig.
   *
   * @param todoModel todoModel instance to serve
   */
  public TodoConfig(TodoModel todoModel) {
    setTodoModel(todoModel);
    register(TodoModelService.class);
    register(TodoModuleObjectMapperProvider.class);
    register(JacksonFeature.class);
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(TodoConfig.this.todoModel);
      }
    });
  }

  /**
   * Initialize this TodoConfig with a default TodoModel.
   */
  public TodoConfig() {
    this(createDefaultTodoModel());
  }

  public TodoModel getTodoModel() {
    return todoModel;
  }

  public void setTodoModel(TodoModel todoModel) {
    this.todoModel = todoModel;
  }

  private static TodoModel createDefaultTodoModel() {
    TodoPersistence todoPersistence = new TodoPersistence();
    try (InputStream input = TodoConfig.class.getResourceAsStream("default-todomodel.json")) {
      return todoPersistence.readTodoModel(new InputStreamReader(input, StandardCharsets.UTF_8));
    } catch (IOException e) {
      System.out.println("Couldn't read default-todomodel.json, so rigging TodoModel manually ("
          + e + ")");
    }
    TodoModel todoModel = new TodoModel();
    todoModel.addTodoList(new TodoList("todo1"));
    todoModel.addTodoList(new TodoList("todo2"));
    return todoModel;
  }
}
