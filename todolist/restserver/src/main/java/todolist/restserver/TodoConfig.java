package todolist.restserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
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
    try {
      return todoPersistence
          .readTodoModel(new InputStreamReader(TodoConfig.class.getResourceAsStream("default-todomodel.json"), StandardCharsets.UTF_8));
    } catch (IOException e) {
      System.out.println("Couldn't read default-todomodel.json, so rigging TodoModel manually (" + e + ")");
    }
    TodoModel todoModel = new TodoModel();
    TodoList todoList1 = new TodoList();
    todoList1.setName("todo1");
    todoModel.addTodoList(todoList1);
    TodoList todoList2 = new TodoList();
    todoList2.setName("todo2");
    todoModel.addTodoList(todoList2);

    return todoModel;
  }
}
