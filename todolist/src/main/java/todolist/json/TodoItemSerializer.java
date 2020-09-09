package todolist.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import todolist.core.TodoItem;

public class TodoItemSerializer extends JsonSerializer<TodoItem> {

    /*
     * format: { "text": "...", "checked": false }
     */

    @Override
    public void serialize(TodoItem item, JsonGenerator jGen, SerializerProvider serializerProvider) throws IOException {
        jGen.writeStartObject();
        jGen.writeStringField("text", item.getText());
        jGen.writeBooleanField("checked", item.isChecked());
        jGen.writeEndObject();
    }
}
