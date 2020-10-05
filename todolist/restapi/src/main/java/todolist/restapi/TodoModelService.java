package todolist.restapi;

import jakarta.inject.Inject;
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

  private TodoList getTodoList(String name, boolean throwWhenMissing) {
    for (TodoList todoList : getTodoModel()) {
      if (name.equals(todoList.getName())) {
        return todoList;
      }
    }
    if (throwWhenMissing) {
      throw new IllegalArgumentException("No TodoList by the name " + name);
    }
    return null;
  }

  /**
   * Returns the TodoList with the provided name.
   *
   * @param name the name of the todo list
   */
  @GET
  @Path("/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  public TodoList getTodoList(@PathParam("name") String name) {
    return getTodoList(name, true);
  }

  private boolean putTodoList(String name, TodoList todoList) {
    TodoList existingTodoList = getTodoList(name, false);
    boolean added = false;
    if (existingTodoList == null) {
      existingTodoList = new TodoList();
      existingTodoList.setName(name);
      getTodoModel().addTodoList(existingTodoList);
      added = true;
    }
    if (todoList != null) {
      existingTodoList.setDeadline(todoList.getDeadline());
    }
    return added;
  }

  /**
   * Adds a TodoList with the given name, if it doesn't exist already.
   *
   * @param name the name of the todo list to add
   */
  @PUT
  @Path("/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean putTodoList(@PathParam("name") String name) {
    return putTodoList(name, null);
  }

  /**
   * Adds or modifies the TodoList with the given name, and sets the deadline.
   *
   * @param todoList the todoList to add or modify
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean putTodoList(TodoList todoList) {
    return putTodoList(todoList.getName(), todoList);
  }

  /**
   * Renames a TodoList.
   *
   * @param fromName the oldName
   * @param toName the newName
   */
  @POST
  @Path("/rename")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean renameTodoList(@QueryParam("from") String fromName, @QueryParam("from") String toName) {
    TodoList todoList = getTodoList(fromName, true);
    todoList.setName(toName);
    return true;
  }

  /**
   * Removes the TodoList by the given name.
   *
   * @param name the name
   */
  @DELETE
  @Path("/{name}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean removeTodoList(String name) {
    TodoList todoList = getTodoList(name, true);
    getTodoModel().removeTodoList(todoList);
    return true;
  }
}
