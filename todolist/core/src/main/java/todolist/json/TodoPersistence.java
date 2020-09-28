package todolist.json;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import com.fasterxml.jackson.databind.ObjectMapper;
import todolist.core.TodoList;

public class TodoPersistence {

  private ObjectMapper mapper;

  public TodoPersistence() {
    mapper = new ObjectMapper();
    mapper.registerModule(new TodoModule());
  }

  public TodoList readTodoList(Reader reader) throws IOException {
    return mapper.readValue(reader, TodoList.class);
  }

  public void writeTodoList(TodoList todoList, Writer writer) throws IOException {
    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, todoList);
  }
}
