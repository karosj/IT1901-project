package todolist.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import todolist.core.TodoItem;
import todolist.core.TodoList;

public class TodoListDeserializer extends JsonDeserializer<TodoList> {

  private TodoItemDeserializer todoItemDeserializer = new TodoItemDeserializer();
  /*
   * format: { "items": [ ... ] }
   */

  @Override
  public TodoList deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    if (treeNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) treeNode;
      TodoList list = new TodoList();
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