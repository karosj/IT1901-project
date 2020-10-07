package todolist.restapi;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import todolist.core.TodoList;
import todolist.core.TodoModel;

@Path(TodoModelService.TODO_MODEL_SERVICE_PATH)
public class TodoModelService {

  public static final String TODO_MODEL_SERVICE_PATH = "todo";

  @Inject
  private TodoModel todoModel;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public TodoModel getTodoModel() {
    return todoModel;
  }

  /**
   * Returns the TodoList with the provided name
   * (as a resource to support chaining path elements).
   *
   * @param name the name of the todo list
   */
  @Path("/{name}")
  public TodoListResource getTodoList(@PathParam("name") String name) {
    TodoList todoList = getTodoModel().getTodoList(name);
    return new TodoListResource(todoModel, name, todoList);
  }
}
