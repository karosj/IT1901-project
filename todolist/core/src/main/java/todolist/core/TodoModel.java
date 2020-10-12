package todolist.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class TodoModel implements Iterable<TodoList> {

  private List<TodoList> todoLists = new ArrayList<>();

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

  /**
   * Replaces an existing TodoList with the same name, or adds it.
   *
   * @param todoList
   * @return the replaced TodoList, or null
   */
  public TodoList putTodoList(TodoList todoList) {
    for (int i = 0; i < todoLists.size(); i++) {
      TodoList oldTodoList = todoLists.get(i);
      if (Objects.equals(oldTodoList.getName(), todoList.getName())) {
        todoLists.set(i, todoList);
        return oldTodoList;
      }
    }
    return null;
  }
}
