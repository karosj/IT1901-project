package todolist.json;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import com.fasterxml.jackson.databind.ObjectMapper;
import todolist.core.TodoModel;

public class TodoPersistence {

  private ObjectMapper mapper;

  public TodoPersistence() {
    mapper = new ObjectMapper();
    mapper.registerModule(new TodoModule());
  }

  public TodoModel readTodoModel(Reader reader) throws IOException {
    return mapper.readValue(reader, TodoModel.class);
  }

  public void writeTodoModel(TodoModel todoModel, Writer writer) throws IOException {
    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, todoModel);
  }
}
