package todolist.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import todolist.core.TodoSettings;
import todolist.core.TodoSettings.TodoItemsSortOrder;

class TodoSettingsDeserializer extends JsonDeserializer<TodoSettings> {

  @Override
  public TodoSettings deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  TodoSettings deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      TodoSettings settings = new TodoSettings();
      JsonNode todoItemsSortOrderNode = objectNode.get("todoItemsSortOrder");
      if (todoItemsSortOrderNode instanceof TextNode) {
        try {
          TodoItemsSortOrder sortOrder = TodoItemsSortOrder.valueOf(todoItemsSortOrderNode.asText());
          settings.setTodoItemSortOrder(sortOrder);
        } catch (IllegalArgumentException iae) {
          // ignore unknown sort order constant
        }
      }
      return settings;
    }
    return null;
  }   
}