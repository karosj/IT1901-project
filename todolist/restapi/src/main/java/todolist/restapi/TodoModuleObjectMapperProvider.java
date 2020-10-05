package todolist.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import todolist.json.TodoModule;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoModuleObjectMapperProvider implements ContextResolver<ObjectMapper> {

  private final ObjectMapper objectMapper;

  public TodoModuleObjectMapperProvider() {
    objectMapper = new ObjectMapper().registerModule(new TodoModule());
  }

  @Override
  public ObjectMapper getContext(final Class<?> type) {
    return objectMapper;
  }
}
