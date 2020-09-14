package todolist.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import todolist.core.TodoItem;
import todolist.core.TodoList;

@SuppressWarnings("serial")
public class TodoModule extends SimpleModule {

    private static final String NAME = "TodoModule";

    public TodoModule() {
        super(NAME, Version.unknownVersion());
        addSerializer(TodoItem.class, new TodoItemSerializer());
        addSerializer(TodoList.class, new TodoListSerializer());
        addDeserializer(TodoItem.class, new TodoItemDeserializer());
        addDeserializer(TodoList.class, new TodoListDeserializer());
    }

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new TodoModule());
        TodoList list = new TodoList();
        TodoItem item1 = new TodoItem();
        item1.setText("item1");
        TodoItem item2 = new TodoItem();
        item2.setText("item2");
        item2.setChecked(true);
        list.addTodoItem(item1);
        list.addTodoItem(item2);
        try {
            String json = mapper.writeValueAsString(list);
            TodoList list2 = mapper.readValue(json, TodoList.class);
            for (TodoItem item : list2) {
                System.out.println(item);
            }
        } catch (JsonProcessingException e) {
            System.err.println("Virket ikke");
            e.printStackTrace();
        }
    }
}