package todolist.json.internal;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import todolist.core.AbstractTodoList;
import todolist.core.TodoItem;
import todolist.core.TodoModel;
import todolist.core.TodoSettings;

/**
 * A Jackson module for configuring JSON serialization of TodoModel instances.
 */
@SuppressWarnings("serial")
public class TodoModule extends SimpleModule {

  private static final String NAME = "TodoModule";

  /**
   * Initializes this TodoModule with appropriate serializers and deserializers.
   */
  public TodoModule(boolean deepTodoModelSerializer) {
    super(NAME, Version.unknownVersion());
    addSerializer(TodoItem.class, new TodoItemSerializer());
    addDeserializer(TodoItem.class, new TodoItemDeserializer());

    addSerializer(AbstractTodoList.class, new TodoListSerializer());
    addDeserializer(AbstractTodoList.class, new TodoListDeserializer());

    addSerializer(TodoModel.class, new TodoModelSerializer(deepTodoModelSerializer));
    addDeserializer(TodoModel.class, new TodoModelDeserializer());

    addSerializer(TodoSettings.class, new TodoSettingsSerializer());
    addDeserializer(TodoSettings.class, new TodoSettingsDeserializer());
  }

  public TodoModule() {
    this(true);
  }
}
