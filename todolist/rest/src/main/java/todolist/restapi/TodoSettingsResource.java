package todolist.restapi;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todolist.core.TodoModel;
import todolist.core.TodoSettings;

/**
 * Used for all requests referring to TodoLists by name.
 */
public class TodoSettingsResource {

  private static final Logger LOG = LoggerFactory.getLogger(TodoListResource.class);

  private final TodoModel todoModel;

  /**
   * Initializes this TodoSettingsResource with appropriate context information.
   * Each method will check and use what it needs.
   *
   * @param todoModel the TodoModel
   */
  public TodoSettingsResource(TodoModel todoModel) {
    this.todoModel = todoModel;
  }

  /**
   * Gets the TodoSettings.
   *
   * @return the TodoSettings
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public TodoSettings getTodoSettings() {
    LOG.debug("getTodoSettings()");
    return this.todoModel.getSettings();
  }

  /**
   * Replaces the TodoSettings.
   *
   * @param todoSettings the new TodoSettings to use
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public void putTodoSettings(TodoSettings todoSettings) {
    LOG.debug("putTodoSettings({})", todoSettings);
    this.todoModel.setSettings(todoSettings);
  }
}
