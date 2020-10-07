package todolist.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TodoModel implements Iterable<TodoList> {

  private Collection<TodoList> todoLists = new ArrayList<>();

  @Override
  public String toString() {
    return String.format("[TodoModel #todoLists=%s]", todoLists.size());
  }

  public void addTodoList(TodoList list) {
    todoLists.add(list);
  }

  public void removeTodoList(TodoList list) {
    todoLists.remove(list);
  }

  @Override
  public Iterator<TodoList> iterator() {
    return todoLists.iterator();
  }

  /**
   * Gets the TodoList with the provided name.
   *
   * @param name the name
   * @return the TodoList with the provided name
   */
  public TodoList getTodoList(String name) {
    return todoLists.stream()
        .filter(todoList -> name.equals(todoList.getName()))
        .findFirst()
        .orElse(null);
  }
}
