package todolist.core;

import java.util.ArrayList;
import java.util.Collection;

public class TodoModel {

  private Collection<TodoList> todoLists = new ArrayList<>();

  public void addTodoList(TodoList list) {
    todoLists.add(list);
  }

  public void removeTodoList(TodoList list) {
    todoLists.add(list);
  }
}