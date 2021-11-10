package todolist.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import todolist.core.AbstractTodoList;
import todolist.core.TodoItem;
import todolist.core.TodoList;
import todolist.core.TodoModel;
import todolist.core.TodoSettings.TodoItemsSortOrder;

public class TodoPersistenceTest {

  private TodoPersistence todoPersistence = new TodoPersistence();

  private TodoModel createSampleTodoModel() {
    TodoModel model = new TodoModel();
    TodoList list = new TodoList("todo");
    model.addTodoList(list);
    TodoItem item1 = new TodoItem();
    item1.setText("item1");
    list.addTodoItem(item1);
    TodoItem item2 = new TodoItem();
    item2.setText("item2");
    item2.setChecked(true);
    item2.setDeadline(LocalDateTime.parse("2020-10-01T14:53:11"));
    list.addTodoItem(item2);
    model.getSettings().setTodoItemsSortOrder(TodoItemsSortOrder.CHECKED_UNCHECKED);
    return model;
  }

  private void checkSampleTodoModel(TodoModel model1, TodoModel model2) {
      Iterator<AbstractTodoList> it1 = model1.iterator();
      Iterator<AbstractTodoList> it2 = model2.iterator();
      assertTrue(it1.hasNext());
      assertTrue(it2.hasNext());
      AbstractTodoList list1 = model1.iterator().next();
      AbstractTodoList list2 = model2.iterator().next();
      assertEquals(list1.getName(), list2.getName());
      Iterator<TodoItem> itemIt1 = list1.iterator();
      Iterator<TodoItem> itemIt2 = list2.iterator();
      assertTrue(itemIt1.hasNext());
      assertTrue(itemIt2.hasNext());
      TodoModuleTest.checkTodoItem(itemIt2.next(), itemIt1.next());
      assertTrue(itemIt1.hasNext());
      assertTrue(itemIt2.hasNext());
      TodoModuleTest.checkTodoItem(itemIt2.next(), itemIt1.next());
      assertFalse(itemIt1.hasNext());
      assertFalse(itemIt2.hasNext());
  }

  @Test
  public void testSerializersDeserializers_usingStringWriter() {
    TodoModel model = createSampleTodoModel();
    try {
      StringWriter writer = new StringWriter();
      todoPersistence.writeTodoModel(model, writer);
      String json = writer.toString();
      TodoModel model2 = todoPersistence.readTodoModel(new StringReader(json));
      checkSampleTodoModel(model, model2);
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testSerializersDeserializers_usingSaveFile() {
    TodoModel model = createSampleTodoModel();
    // set unique save file path
    todoPersistence.setSaveFile("todo-" + System.currentTimeMillis() + ".json");
    Path saveFilePath = todoPersistence.getSaveFilePath();
    try {
      todoPersistence.saveTodoModel(model);
      assertTrue(Files.exists(saveFilePath));
      TodoModel model2 = todoPersistence.loadTodoModel();
      checkSampleTodoModel(model, model2);
    } catch (IOException e) {
      fail(e.getMessage());
    } finally {
      try {
        Files.deleteIfExists(saveFilePath);
      } catch (IOException e) {
      }
    }
  }
}
