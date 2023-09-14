package todolist.json.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Set;
import todolist.core.AbstractTodoList;
import todolist.core.TodoModel;
import todolist.json.TodoPersistence.TodoModelParts;

class TodoModelSerializer extends JsonSerializer<TodoModel> {

  private final Set<TodoModelParts> parts;

  public TodoModelSerializer(Set<TodoModelParts> parts) {
    this.parts = parts;
  }

  /*
   * format: { "lists": [ ... ] }
   */

  @Override
  public void serialize(TodoModel model, JsonGenerator jsonGen, SerializerProvider
      serializerProvider) throws IOException {
    jsonGen.writeStartObject();
    if (parts.contains(TodoModelParts.LIST_CONTENTS) || parts.contains(TodoModelParts.LISTS)) {
      jsonGen.writeArrayFieldStart("lists");
      for (AbstractTodoList list : model) {
        if (parts.contains(TodoModelParts.LIST_CONTENTS)) {
          jsonGen.writeObject(list);
        } else if (parts.contains(TodoModelParts.LISTS)) {
          jsonGen.writeStartObject();
          jsonGen.writeStringField("name", list.getName());
          if (list.getDeadline() != null) {
            jsonGen.writeStringField("deadline", list.getDeadline().toString());
          }
          // no items!
          jsonGen.writeEndObject();
        }
      }
      jsonGen.writeEndArray();
    }
    if (parts.contains(TodoModelParts.SETTINGS)) {
      jsonGen.writeFieldName("settings");
      jsonGen.writeObject(model.getSettings());
    }
    jsonGen.writeEndObject();
  }
}