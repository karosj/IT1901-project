package todolist.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import java.time.LocalDateTime;
import todolist.core.TodoItem;
import todolist.core.TodoList;

class TodoListDeserializer extends JsonDeserializer<TodoList> {

  private TodoItemDeserializer todoItemDeserializer = new TodoItemDeserializer();
  /*
   * format: { "items": [ ... ] }
   */

  @Override
  public TodoList deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  TodoList deserialize(JsonNode treeNode) {
    if (treeNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) treeNode;
      TodoList list = new TodoList();
      JsonNode nameNode = objectNode.get("name");
      if (nameNode instanceof TextNode) {
        list.setName(nameNode.asText());
      }
      JsonNode deadlineNode = objectNode.get("deadline");
      if (deadlineNode instanceof TextNode) {
        list.setDeadline(LocalDateTime.parse(deadlineNode.asText()));
      }
      JsonNode itemsNode = objectNode.get("items");
      if (itemsNode instanceof ArrayNode) {
        for (JsonNode elementNode : ((ArrayNode) itemsNode)) {
          TodoItem item = todoItemDeserializer.deserialize(elementNode);
          if (item != null) {
            list.addTodoItem(item);
          }
        }
      }
      return list;
    }
    return null;
  }   
}