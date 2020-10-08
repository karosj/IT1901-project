package todolist.ui;

import java.util.ArrayList;
import java.util.Collection;
import todolist.core.TodoList;
import todolist.core.TodoModel;

/**
 * Class that centralizes access to a TodoModel.
 * Makes it easier to support transparent use of a REST API.
 */
public class DirectTodoModelAccess implements TodoModelAccess {

  private final TodoModel todoModel;

  public DirectTodoModelAccess(TodoModel todoModel) {
    this.todoModel = todoModel;
  }

  /**
   * Gets the names of the TodoLists.
   * @return the names of the TodoLists.
   */
  public Collection<String> getTodoListNames() {
    Collection<String> allNames = new ArrayList<>();
    todoModel.forEach(todoList -> allNames.add(todoList.getName()));
    return allNames;
  }

  /**
   * Gets the TodoList with the given name.
   *
   * @param name the TodoList's name
   * @return the TodoList with the given name
   */
  public TodoList getTodoList(String name) {
    return todoModel.getTodoList(name);
  }

  /**
   * Adds a TodoList to the underlying TodoModel
   *
   * @param todoList the TodoList
   */
  public void addTodoList(TodoList todoList) {
    todoModel.addTodoList(todoList);
  }

  /**
   * Removes the TodoList with the given name from the underlying TodoModel.
   *
   * @param todoList the TodoList
   */
  public void removeTodoList(String name) {
    todoModel.removeTodoList(todoModel.getTodoList(name));
  }

  /**
   * Renames a TodoList to a new name
   *
   * @param oldName the name of the TodoList to change
   * @param newName the new name
   */
  public void renameTodoList(String oldName, String newName) {
    TodoList todoList = todoModel.getTodoList(oldName);
    if (todoList == null) {
      throw new IllegalArgumentException("No TodoList named \"" + newName + "\" found");
    }
    if (todoModel.getTodoList(newName) != null) {
      throw new IllegalArgumentException("A TodoList named \"" + newName + "\" already exists");
    }
    todoList.setName(newName);
  }

  /**
   * Notifies that the TodoList has changed, e.g. TodoItems
   * have been mutated, added or removed.
   *
   * @param todoList the TodoList that has changed
   */
  public void notifyTodoListChanged(TodoList todoList) {
    // no need to do anything locally
  }
}