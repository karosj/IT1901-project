package todolist.json.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import todolist.core.TodoSettings;

class TodoSettingsSerializer extends JsonSerializer<TodoSettings> {

  /*
   * format: { "todoItemsSortOrder": "..." }
   */

  @Override
  public void serialize(TodoSettings settings, JsonGenerator jsonGen,
      SerializerProvider serializerProvider)
      throws IOException {
    jsonGen.writeStartObject();
    jsonGen.writeStringField(TodoSettings.TODO_ITEM_SORT_ORDER_SETTING, settings.getTodoItemsSortOrder().name());
    jsonGen.writeEndObject();
  }
}
