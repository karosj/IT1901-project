package todolist.ui;

import java.util.Collection;
import todolist.core.TodoList;

/**
 * Class that centralizes access to a TodoModel.
 * Makes it easier to support transparent use of a REST API.
 */
public interface TodoModelAccess {

  /**
   * Gets the names of the TodoLists.
   *
   * @return the names of the TodoLists.
   */
  Collection<String> getTodoListNames();

  /**
   * Gets the TodoList with the given name.
   *
   * @param name the TodoList's name
   * @return the TodoList with the given name
   */
  TodoList getTodoList(String name);

  /**
   * Adds a TodoList to the underlying TodoModel.
   *
   * @param todoList the TodoList
   */
  void addTodoList(TodoList todoList);

  /**
   * Removes the TodoList with the given name from the underlying TodoModel.
   *
   * @param name the name of the TodoList to remove
   */
  void removeTodoList(String name);

  /**
   * Renames the TodoList to the newName.
   *
   * @param oldName the name of the TodoList to rename
   * @param newName the new name
   */
  void renameTodoList(String oldName, String newName);

  /**
   * Notifies that the TodoList has changed, e.g. TodoItems
   * have been mutated, added or removed.
   *
   * @param todoList the TodoList that has changed
   */
  void notifyTodoListChanged(TodoList todoList);
}