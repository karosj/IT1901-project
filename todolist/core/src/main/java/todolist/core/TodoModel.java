package todolist.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TodoModel implements Iterable<TodoList> {

  private Collection<TodoList> todoLists = new ArrayList<>();

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
}
