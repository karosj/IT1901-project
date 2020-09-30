package todolist.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import todolist.core.TodoList;
import todolist.core.TodoModel;

class TodoModelSerializer extends JsonSerializer<TodoModel> {

  /*
   * format: { "lists": [ ... ] }
   */

  @Override
  public void serialize(TodoModel model, JsonGenerator jsonGen, SerializerProvider serializerProvider)
      throws IOException {
    jsonGen.writeStartObject();
    jsonGen.writeArrayFieldStart("lists");
    for (TodoList list : model) {
      jsonGen.writeObject(list);
    }
    jsonGen.writeEndArray();
    jsonGen.writeEndObject();
  }
}