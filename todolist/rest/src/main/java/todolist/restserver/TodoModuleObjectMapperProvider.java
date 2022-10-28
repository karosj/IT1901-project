package todolist.restserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import java.util.EnumSet;
import todolist.json.TodoPersistence;
import todolist.json.TodoPersistence.TodoModelParts;

/**
 * Provides the Jackson module used for JSON serialization.
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TodoModuleObjectMapperProvider implements ContextResolver<ObjectMapper> {

  private final ObjectMapper objectMapper;
  
  /**
   * Constructor
   * use variant which only serializes list 
   * properties as part of TodoModel objects, 
   * and not the contents, nor settings.
   * The contents or settings are serialized as 
   * part of TodoList and TodoSettings objects
   */
  public TodoModuleObjectMapperProvider() {
    objectMapper = TodoPersistence.createObjectMapper(EnumSet.of(TodoModelParts.LISTS));
  }

  @Override
  public ObjectMapper getContext(final Class<?> type) {
    return objectMapper;
  }
}
