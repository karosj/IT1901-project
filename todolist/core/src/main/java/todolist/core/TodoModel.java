package todolist.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import todolist.core.TodoSettings.TodoItemsSortOrder;

/**
 * The root container of Todo-related data.
 */
public class TodoModel implements Iterable<AbstractTodoList> {

  private TodoSettings settings = new TodoSettings();

  public TodoSettings getSettings() {
    return settings;
  }

  public void setSettings(TodoSettings settings) {
    this.settings = settings;
  }

  private List<AbstractTodoList> todoLists = new ArrayList<>();

  @Override
  public String toString() {
    return String.format("[TodoModel #todoLists=%s]", todoLists.size());
  }

  /**
   * Find the position of a TodoList with the provided name.
   *
   * @param name the name
   * @return the index of the TodoList with the provided name, or -1
   */
  private int indexOfTodoList(String name) {
    for (int i = 0; i < todoLists.size(); i++) {
      if (name.equals(todoLists.get(i).getName())) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Checks if this TodoModel already has a TodoList with the provided name.
   *
   * @param name the name to check
   * @return true if a TodoList with the provided name exists, false otherwise
   */
  public boolean hasTodoList(String name) {
    return indexOfTodoList(name) < 0;
  }

  /**
   * Checks if it is OK to add a list with the provided name.
   *
   * @param name the new name
   * @return true if the name is value, false otherwise
   */
  public boolean isValidTodoListName(String name) {
    return name.strip().length() > 0;
  }

  /**
   * Adds the TodoList to this TodoModel.
   *
   * @param list the TodoList
   * @throws IllegalArgumentException if the list's name is invalid
   */
  public void addTodoList(AbstractTodoList list) {
    if (! isValidTodoListName(list.getName())) {
      throw new IllegalArgumentException(list.getName() + " is not a legal name for a new list");
    }
    if (! todoLists.contains(list)) {
      todoLists.add(list);
    }
  }

  public void removeTodoList(AbstractTodoList list) {
    todoLists.remove(list);
  }

  @Override
  public Iterator<AbstractTodoList> iterator() {
    return todoLists.iterator();
  }

  /**
   * Gets the TodoList with the provided name.
   *
   * @param name the name
   * @return the TodoList with the provided name
   */
  public AbstractTodoList getTodoList(String name) {
    for (var list : todoLists) {
      if (name.equals(list.getName())) {
        return list;
      }
    }
    return null;
  }

  /**
   * Replaces an existing TodoList with the same name, or adds it.
   *
   * @param todoList the TodoList
   * @return the replaced TodoList, or null
   */
  public AbstractTodoList putTodoList(AbstractTodoList todoList) {
    int pos = indexOfTodoList(todoList.getName());
    if (pos >= 0) {
      var oldTodoList = todoLists.get(pos);
      todoLists.set(pos, todoList);
      return oldTodoList;
    } else {
      todoLists.add(todoList);
      return null;
    }
  }

  private static Collection<TodoItem> todoItemsProviderHelper(TodoList todoList,
      Function<TodoList, Collection<TodoItem>> todoItemsProvider1,
      Function<TodoList, Collection<TodoItem>> todoItemsProvider2) {
    Collection<TodoItem> todoItems = new ArrayList<>(todoItemsProvider1.apply(todoList));
    todoItems.addAll(todoItemsProvider2.apply(todoList));
    return todoItems;
  }

  /**
   * Returns a function that gets the todo items of a todo list in the corresponding sort order.
   *
   * @param sortOrder the desired sort order of the todo items
   * @return a function that gets the todo items of a todo list in the corresponding sort order
   */
  public static Function<TodoList, Collection<TodoItem>> getSortedTodoItemsProvider(TodoItemsSortOrder sortOrder) {
    switch (sortOrder) {
      case UNCHECKED_CHECKED: 
        return todoList -> todoItemsProviderHelper(todoList,
            TodoList::getUncheckedTodoItems, TodoList::getCheckedTodoItems);
      case CHECKED_UNCHECKED:
        return todoList -> todoItemsProviderHelper(todoList,
            TodoList::getCheckedTodoItems, TodoList::getUncheckedTodoItems);
      default: return TodoList::getTodoItems;
    }
  }

  /**
   * Returns a function that gets the todo items of a todo list
   * in the sort order given by the todo settings.
   *
   * @return a function that gets the todo items of a todo list in the corresponding sort order
   */
  public Function<TodoList, Collection<TodoItem>> getSortedTodoItemsProvider() {
    return getSortedTodoItemsProvider(getSettings().getTodoItemsSortOrder());
  }
}
