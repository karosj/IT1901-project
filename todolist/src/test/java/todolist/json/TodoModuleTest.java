package todolist.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import todolist.core.TodoItem;
import todolist.core.TodoList;

public class TodoModuleTest {

  // {"items":[{"text":"item1","checked":false},{"text":"item2","checked":true}]}

  private static ObjectMapper mapper;

  @BeforeAll
  public static void setUp() {
    mapper = new ObjectMapper();
    mapper.registerModule(new TodoModule());
  }

  private final static String todoListWithTwoItems = "{\"items\":[{\"text\":\"item1\",\"checked\":false},{\"text\":\"item2\",\"checked\":true}]}";

  @Test
  public void testSerializers() {
    TodoList list = new TodoList();
    TodoItem item1 = list.createTodoItem();
    item1.setText("item1");
    TodoItem item2 = list.createTodoItem();
    item2.setText("item2");
    item2.setChecked(true);
    list.addTodoItem(item1);
    list.addTodoItem(item2);
    try {
      assertEquals(todoListWithTwoItems.replaceAll("\\s+", ""), mapper.writeValueAsString(list));
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  static void checkTodoItem(TodoItem item, String text, boolean checked) {
    assertEquals(text, item.getText());
    assertTrue(checked == item.isChecked());
  }

  static void checkTodoItem(TodoItem item1, TodoItem item2) {
    checkTodoItem(item1, item2.getText(), item2.isChecked());
  }

  @Test
  public void testDeserializers() {
    try {
      TodoList list = mapper.readValue(todoListWithTwoItems, TodoList.class);
      Iterator<TodoItem> it = list.iterator();
      assertTrue(it.hasNext());
      checkTodoItem(it.next(), "item1", false);
      assertTrue(it.hasNext());
      checkTodoItem(it.next(), "item2", true);
      assertFalse(it.hasNext());
    } catch (JsonProcessingException e) {
      fail();
    }
  }

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
      String json = mapper.writeValueAsString(list);
      TodoList list2 = mapper.readValue(json, TodoList.class);
      Iterator<TodoItem> it = list2.iterator();
      assertTrue(it.hasNext());
      checkTodoItem(it.next(), item1);
      assertTrue(it.hasNext());
      checkTodoItem(it.next(), item2);
      assertFalse(it.hasNext());
    } catch (JsonProcessingException e) {
      fail();
    }
  }
}