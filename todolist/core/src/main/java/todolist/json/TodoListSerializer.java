package todolist.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import todolist.core.TodoItem;
import todolist.core.TodoList;

class TodoListSerializer extends JsonSerializer<TodoList> {

  /*
   * format: { "items": [ ... ] }
   */

  @Override
  public void serialize(TodoList list, JsonGenerator jsonGen, SerializerProvider serializerProvider)
      throws IOException {
    jsonGen.writeStartObject();
    if (list.getDeadline() != null) {
      jsonGen.writeStringField("deadline", list.getDeadline().toString());
    }
    jsonGen.writeArrayFieldStart("items");
    for (TodoItem item : list) {
      jsonGen.writeObject(item);
    }
    jsonGen.writeEndArray();
    jsonGen.writeEndObject();
  }
}
