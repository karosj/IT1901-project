package todolist.ui;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import todolist.core.TodoItem;
import todolist.core.TodoList;
import todolist.json.TodoModule;

public class TodoController {

    private final static String todoListWithTwoItems = "{\"items\":[{\"text\":\"item1\",\"checked\":false},{\"text\":\"item2\",\"checked\":true}]}";

    private TodoList todoList;
    private ObjectMapper mapper = new ObjectMapper();

    public TodoController() {
        // setter opp data
        mapper.registerModule(new TodoModule());
        try {
            todoList = mapper.readValue(todoListWithTwoItems, TodoList.class);
        } catch (JsonProcessingException e) {
        }

    }

    @FXML
    TextField newTodoItemText;

    @FXML
    ListView<TodoItem> todoListView;

    @FXML
    Button deleteTodoItemButton;

    @FXML
    Button checkTodoItemButton;

    private Collection<Button> selectionButtons;

    @FXML
    public void initialize() {
        selectionButtons = List.of(deleteTodoItemButton, checkTodoItemButton);
        // kobler data til list-controll
        updateTodoListView();
        updateTodoListButtons();
        todoList.addTodoListListener(todoList -> updateTodoListView());
        todoListView.setCellFactory(listView -> new TodoItemListCell());
        todoListView.getSelectionModel().selectedItemProperty().addListener((prop, oldValue, newValue) -> updateTodoListButtons());
    }

    protected void updateTodoListView() {
        List<TodoItem> viewList = todoListView.getItems();
        viewList.clear();
        viewList.addAll(todoList.getUncheckedTodoItems());
        viewList.addAll(todoList.getCheckedTodoItems());
    }

    private void updateTodoListButtons() {
        boolean disable = todoListView.getSelectionModel().getSelectedItem() == null;
        for (Button button : selectionButtons) {
            button.setDisable(disable);
        }
    }

    @FXML
    public void handleNewTodoItemAction() {
        TodoItem item = todoList.createTodoItem();
        item.setText(newTodoItemText.getText());
        todoList.addTodoItem(item);
    }



    @FXML
    public void handleDeleteItemAction() {
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        if (item != null) {
            todoList.removeTodoItem(item);
        }
    }

    @FXML
    public void handleCheckItemAction() {
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        if (item != null) {
            item.setChecked(true);
        }
    }
}
