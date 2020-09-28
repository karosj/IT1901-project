package todolist.json;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import todolist.core.TodoItem;
import todolist.core.TodoList;

public class TodoPersistenceTest {

  private TodoPersistence todoPersistence = new TodoPersistence();

  @Test
  public void testSerializersDeserializers() {
    TodoList list = new TodoList();
    TodoItem item1 = new TodoItem();
    item1.setText("item1");
    TodoItem item2 = new TodoItem();
    item2.setText("item2");
    item2.setChecked(true);
    list.addTodoItem(item1);
    list.addTodoItem(item2);
    try {
      StringWriter writer = new StringWriter();
      todoPersistence.writeTodoList(list, writer);
      String json = writer.toString();
      TodoList list2 = todoPersistence.readTodoList(new StringReader(json));
      Iterator<TodoItem> it = list2.iterator();
      assertTrue(it.hasNext());
      TodoModuleTest.checkTodoItem(it.next(), item1);
      assertTrue(it.hasNext());
      TodoModuleTest.checkTodoItem(it.next(), item2);
      assertFalse(it.hasNext());
    } catch (IOException e) {
      fail();
    }
  }
}