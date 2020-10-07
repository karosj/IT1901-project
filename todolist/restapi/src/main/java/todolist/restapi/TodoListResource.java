package todolist.restapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import todolist.core.TodoList;
import todolist.core.TodoModel;

public class TodoListResource {

  private final TodoModel todoModel;
  private final String name;
  private final TodoList todoList;

  public TodoListResource(TodoModel todoModel, String name, TodoList todoList) {
    this.todoModel = todoModel;
    this.name = name;
    this.todoList = todoList;
  }

  private void checkTodoList() {
    if (this.todoList == null) {
      throw new IllegalArgumentException("No TodoList named \"" + name + "\"");
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public TodoList gxetTodoList() {
    checkTodoList();
    return this.todoList;
  }

  /**
   * Adds a TodoList, if it doesn't exist already.
   *
   * @param todoListArg the todoList to add
   */
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public boolean putTodoList(TodoList todoListArg) {
    TodoList todoList = null;
    if (this.todoList != null) {
      todoList = this.todoList;
    } else {
      todoList = new TodoList();
      todoList.setName(this.name);
      todoModel.addTodoList(todoList);
    }
    if (todoListArg != null) {
      todoList.setDeadline(todoListArg.getDeadline());
    }
    return this.todoList == null;
  }

  /**
   * Adds a TodoList with the given name, if it doesn't exist already.
   */
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  public boolean putTodoList() {
    return putTodoList(null);
  }

  /**
   * Renames the TodoList.
   *
   * @param newName the newName
   */
  @POST
  @Path("/rename")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean renameTodoList(@QueryParam("newName") String newName) {
    checkTodoList();
    if (this.todoModel.getTodoList(newName) != null) {
      throw new IllegalArgumentException("A TodoList named \"" + newName + "\" already exists");
    }
    this.todoList.setName(newName);
    return true;
  }

  /**
   * Removes the TodoList.
   */
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  public boolean removeTodoList() {
    checkTodoList();
    this.todoModel.removeTodoList(this.todoList);
    return true;
  }
}
