package todolist.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import todolist.core.TodoItem;

public class TodoItemDeserializer extends JsonDeserializer<TodoItem> {

  @Override
  public TodoItem deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  TodoItem deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      TodoItem item = new TodoItem();
      JsonNode textNode = objectNode.get("text");
      if (textNode instanceof TextNode) {
        item.setText(((TextNode) textNode).asText());
      }
      JsonNode checkedNode = objectNode.get("checked");
      if (checkedNode instanceof BooleanNode) {
        item.setChecked(((BooleanNode) checkedNode).asBoolean(false));
      }
      return item;
    }
    return null;
  }   
}