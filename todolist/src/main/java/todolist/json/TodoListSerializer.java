package todolist.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import todolist.core.TodoItem;
import todolist.core.TodoList;

public class TodoListSerializer extends JsonSerializer<TodoList> {

    /*
     * format: { "items": [ ... ] }
     */

    @Override
    public void serialize(TodoList list, JsonGenerator jGen, SerializerProvider serializerProvider) throws IOException {
        jGen.writeStartObject();
        jGen.writeArrayFieldStart("items");
        for (TodoItem item : list) {
            jGen.writeObject(item);
        }
        jGen.writeEndArray();
        jGen.writeEndObject();
    }
}
